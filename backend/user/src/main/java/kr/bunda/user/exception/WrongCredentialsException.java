package kr.bunda.user.exception;

public class WrongCredentialsException extends Exception {
    private static final long serialVersionUID = 1101094665446419676L;
    Object[] parameter;

    public WrongCredentialsException(String msg, Object... parameter) {
        super((msg));
        this.parameter = parameter;
    }

    public Object[] getParameter() {
        return parameter;
    }

    public String getLog() {
        if (parameter == null) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (Object object : parameter) {
            sb.append(object).append("\n");
        }
        return sb.toString();
    }
}
