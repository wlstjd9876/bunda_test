package kr.bunda.user.exception;

public class InvalidArgumentException extends Exception {
    private static final long serialVersionUID = 6436992652146648944L;

    Object[] parameter;

    public InvalidArgumentException(String msg, Object... parameter) {
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
