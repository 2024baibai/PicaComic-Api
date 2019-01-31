package com.flannep.picacomic.api.users;

import net.sf.json.JSONObject;

/**
 * 代表一个用户的个人信息
 * 注意：资料分为自己的资料和他人的资料
 * 他人的资料包含较多信息，但不包含签到
 * 自己的资料信息较少，包含签到
 */
public class UserProfile {

    private JSONObject user;

    public UserProfile(JSONObject data) {
        if (data.containsKey("user")) {
            this.user = data.getJSONObject("user");
        } else {
            throw new IllegalArgumentException("非法的构造参数");
        }
    }

    @Override
    public String toString() {
        return user.toString();
    }


    /**
     * 判断是否已签到
     * 注意：仅当个人资料是自己的资料时才能获取到！
     *
     * @return 获取失败固定返回false
     */
    public boolean isPunched() {
        if (user.containsKey("isPunched")) {
            return user.getBoolean("isPunched");
        }
        return false;
    }

    /**
     * 获取用户的内部ID（唯一）
     * 样例：582995d32cff435850358afa
     *
     * @return 不存在此字段返回null
     */
    public String getID() {
        if (user.containsKey("_id")) {
            return user.getString("_id");
        }
        return null;
    }


    /**
     * 获取用户设置的生日
     * （截止2019/01/28，用户暂无法自行更改）
     * 样例：1992-06-06T00:00:00.000Z
     */
    public String getBitrhday() {
        if (user.containsKey("birthday")) {
            return user.getString("birthday");
        }
        return null;
    }

    /**
     * 获取用户的注册邮箱
     */
    public String getEmail() {
        if (user.containsKey("email")) {
            return user.getString("email");
        }
        return null;
    }

    /**
     * 获取用户设置的性别
     * m或者f?有存在字段为bot的性别
     */
    public String getGender() {
        if (user.containsKey("gender")) {
            return user.getString("gender");
        }
        return null;
    }

    /**
     * 获取用户显示的名称
     * 用户对外显示的名称
     */
    public String getName() {
        if (user.containsKey("name")) {
            return user.getString("name");
        }
        return null;
    }

    /**
     * 获取用户的密码（已加密）
     * 样例：$2a$08$KDFYiXdfLW6tX8hFSOT.2ubCw5qvNzCqHCEdNigCvD1vPfrQQ9vsy
     */
    public String getPassword() {
        if (user.containsKey("password")) {
            return user.getString("password");
        }
        return null;
    }

    /**
     * 未知
     * 样例：8aeff762-aff9-4aed-8353-4227df29a96f
     */
    public String getActivation_code() {
        if (user.containsKey("activation_code")) {
            return user.getString("activation_code");
        }
        return null;
    }

    /**
     * 可能是激活账户的时间
     * 样例：2016-11-24T01:49:16.255Z
     */
    public String getActivation_date() {
        if (user.containsKey("activation_date")) {
            return user.getString("activation_date");
        }
        return null;
    }

    /**
     * 最后登录时间
     * 样例：2018-12-19T02:54:06.347Z
     */
    public String getLast_login_date() {
        if (user.containsKey("last_login_date")) {
            return user.getString("last_login_date");
        }
        return null;
    }

    /**
     * 获取用户的头衔
     * 样例：萌新
     */
    public String getTitle() {
        if (user.containsKey("title")) {
            return user.getString("title");
        }
        return null;
    }

    /**
     * 获取用户的个人签名
     */
    public String getSlogan() {
        if (user.containsKey("slogan")) {
            return user.getString("slogan");
        }
        return null;
    }

    /**
     * 用户的IP地址
     * 尚不确定是注册IP还是最后登录的IP
     */
    public String getIP() {
        if (user.containsKey("ip")) {
            return user.getString("ip");
        }
        return null;
    }

    /**
     * 未知，可能是管理员认证？
     * 普通用户是false，暂时没看到为true的
     */
    public boolean isVerified() {
        if (user.containsKey("ip")) {
            return user.getBoolean("verified");
        }
        return false;
    }

    /**
     * 获取的总经验值
     *
     * @return 当不存在此字段返回-1
     */
    public int getExp() {
        if (user.containsKey("exp")) {
            return user.getInt("exp");
        }
        return -1;
    }

    /**
     * 当前等级
     *
     * @return 当不存在此字段返回-1
     */
    public int getLevel() {
        if (user.containsKey("level")) {
            return user.getInt("level");
        }
        return -1;
    }

    /**
     * 未知，或许是个人资料更新的时间？
     * 样例：2019-01-27T14:21:36.891Z
     */
    public String getUpdated_at() {
        if (user.containsKey("updated_at")) {
            return user.getString("updated_at");
        }
        return null;
    }

    /**
     * 未知，可能是个人资料创建的时间？
     * 这个时间会比activation_date早
     * 可能是刚注册，但尚未在邮箱内确认的时间
     * 样例：2016-11-14T10:45:39.139Z
     */
    public String getCreated_at() {
        if (user.containsKey("created_at")) {
            return user.getString("created_at");
        }
        return null;
    }

    /**
     * 获取头像信息
     */
    public Avatar getAvatar() {
        if (user.containsKey("avatar")) {
            return new Avatar(user.getJSONObject("avatar"));
        }
        return null;
    }

    /**
     * 未知，可能是激活邮件发送次数？
     * 似乎都是0
     */
    public int getResendCount() {
        if (user.containsKey("resendCount")) {
            return user.getInt("resendCount");
        }
        return -1;
    }

    /**
     * 未知，可能是忘记密码的次数？
     * 似乎都是0
     */
    public int getForgotCount() {
        if (user.containsKey("forgotCount")) {
            return user.getInt("forgotCount");
        }
        return -1;
    }

    /**
     * 似乎是用户头像的特效框架
     * 样例：https://www.picacomic.com/characters/frame_knight_500_999.png?r=3
     */
    public String getCharacter() {
        if (user.containsKey("character")) {
            return user.getString("character");
        }
        return null;
    }

    /**
     * 代表个人头像
     */
    class Avatar {

        private JSONObject avatar;

        Avatar(JSONObject avatarObj) {
            this.avatar = avatarObj;
        }

        /**
         * 获取头像文件名
         * 样例：avatar.jpg
         * 基本是这个，没有什么变化
         */
        public String getOriginalName() {
            if (avatar.containsKey("originalName")) {
                return avatar.getString("originalName");
            }
            return null;
        }


        /**
         * 获取图片文件名
         * 样例：c22db0c7-d0ef-4db7-afbe-a4e5929a9843.jpg
         * 如果存放的服务器是https://storage1.picacomic.com
         * 那连接应该是：
         * https://storage1.picacomic.com/static/c22db0c7-d0ef-4db7-afbe-a4e5929a9843.jpg
         */
        public String getPath() {
            if (avatar.containsKey("path")) {
                return avatar.getString("path");
            }
            return null;
        }

        /**
         * 获取存放的服务器地址：
         * 样例：https://storage1.picacomic.com
         */
        public String getFileServer() {
            if (avatar.containsKey("fileServer")) {
                return avatar.getString("fileServer");
            }
            return null;
        }

        /**
         * 判断是否签到
         *
         * @return 如果获取失败固定返回false
         */
        public boolean isPunched() {
            if (user.containsKey("isPunched")) {
                return avatar.getBoolean("isPunched");
            }
            return false;
        }
    }
}