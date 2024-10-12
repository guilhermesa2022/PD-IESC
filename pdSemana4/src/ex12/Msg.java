package ex12;

public class Msg {
    public static final long serialVersionUID = 1010L;

    protected String nickname;
    protected String msg;

    public Msg(String nickname, String msg) {
        this.nickname = nickname;
        this.msg = msg;
    }

    public String getNickname() {
        return nickname;

    }

    public String getMsg() {
        return msg;
    }

    public boolean equals(String string){
        return this.msg.equals(string);
    }
}