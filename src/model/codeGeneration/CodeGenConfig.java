package model.codeGeneration;

public class CodeGenConfig {
    public static final String DATA = ".DATA";
    public static final String CODE = ".CODE";
    public static final String OFFSET_THIS = "3";
    public static final String NULL_VALUE = "0";
    public static final String FALSE_VALUE = "0";
    public static final String TRUE_VALUE = "1";
    public static final int PARAM_OFFSET = Integer.parseInt(OFFSET_THIS);
    public static final String PUSH_MALLOC = Instructions.PUSH + " simple_malloc";
}
