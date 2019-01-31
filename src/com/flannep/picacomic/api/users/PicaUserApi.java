package com.flannep.picacomic.api.users;

import com.flannep.picacomic.api.NetUtil;
import com.flannep.picacomic.api.PicaHeader;
import com.flannep.picacomic.api.PicaResult;
import net.sf.json.JSONObject;

/**
 * 代表用户的api
 * 包含以下功能
 * 1.获取自己/他人的用户信息
 * 2.获取自己喜欢的本子
 * 3.签到
 *
 * @author FlanN
 */
public class PicaUserApi {

    private String authorization;

    public PicaUserApi(String authorization) {
        this.authorization = authorization;
    }

    /**
     * 获取自己喜欢的本子
     *
     * @param page 页码，必须大于0,如果超出范围，则不存在本子，但依然包含其他信息
     * @return
     * @throws Exception 访问出现严重错误时抛出任何异常
     */
    public MyFavorite getMyFavorite(int page) throws Exception {
        PicaHeader header = new PicaHeader();
        header.setUrl(String.format("https://picaapi.picacomic.com/users/favourite?page=%d", page));
        header.setAuthorization(authorization);
        String webContent = NetUtil.sendGet(header, true);
        PicaResult result = new PicaResult(JSONObject.fromObject(webContent));

        if (result.hasError()) {
            if (result.getErrorCode() == 1016) {
                throw new IllegalArgumentException("[错误]无效的请求，请检查页码是否正确。页码: " + page + "\n" + result.toString());
            }
            throw new IllegalAccessException("[错误]无法处理的异常:\n" + result.toString());
        }

        return new MyFavorite(result.getData());
    }


    /**
     * 签到
     * 注意，重复签到服务器都会返回成功，但是不会加经验。
     *
     * @return
     */
    public PicaResult punch_in() throws Exception {
        PicaHeader header = new PicaHeader();
        header.setAuthorization(this.authorization);
        header.setUrl("https://picaapi.picacomic.com/users/punch-in");
        header.setMethod(PicaHeader.Method.POST);
        String webContent = NetUtil.sendGet(header, true);
        return new PicaResult(JSONObject.fromObject(webContent));
    }

    /**
     * 获取自己的个人资料
     * 自己的资料与他人视角资料相比缺少大量信息，仅多了是否签到的字段
     * 如果不需要此字段，建议将othersView设为true以获取更多信息。
     *
     * @param othersView 他人视角
     * @return
     * @throws Exception
     */
    public UserProfile getMyProfile(boolean othersView) throws Exception {

        if (othersView) {
            return getUserProfile(null);
        }

        PicaHeader header = new PicaHeader();
        header.setAuthorization(authorization);
        header.setUrl("https://picaapi.picacomic.com/users/profile");
        PicaResult pr = PicaResult.getPicaResult(header);
        return new UserProfile(pr.getData());

    }

    /**
     * 获取用户的个人资料
     * 注意：如果请求自己的资料，返回的也是他人视角中自己的资料，无法获取到签到信息，请注意！
     * 需要请求自己的资料，请使用getMyProfile()获取！
     *
     * @param targetID 如果ID为空，则返回自己的个人资料，注意不是显示的ID（昵称），是用户的内部ID
     * @return
     */
    public UserProfile getUserProfile(String targetID) throws Exception {

        PicaHeader header = new PicaHeader();
        if (targetID == null) {
            header.setUrl("https://picaapi.picacomic.com/users/profile");
        } else {
            header.setUrl(String.format("https://picaapi.picacomic.com/users/%s/profile", targetID));
        }

        header.setAuthorization(authorization);
        String webContent = NetUtil.sendGet(header, true);
        PicaResult picaResult = new PicaResult(JSONObject.fromObject(webContent));
        if (picaResult.hasError()) {
            throw new IllegalAccessException("[错误]无法处理的异常：无法获取个人资料。\n" + picaResult.toString());
        }

        //如果是查询自己的，就将自己的ID传入，递归获取
        if (targetID == null) {
            targetID = new UserProfile(picaResult.getData()).getID();
            if (targetID == null) {
                throw new IllegalArgumentException("[错误]无法处理的异常：无法获取到用户ID。\n" + picaResult.toString());
            }
            return getUserProfile(targetID);
        }

        return new UserProfile(picaResult.getData());
    }


}
