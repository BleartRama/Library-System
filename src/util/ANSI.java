package util;


    @SuppressWarnings("unused")
    public class ANSI {
        // https://en.wikipedia.org/wiki/ANSI_escape_code#Select_Graphic_Rendition_parameters
        // ANSI escape sequences for styling terminal output

        public static final String RESET = "\033[0m";
        public static final String BOLD = "\033[1m";
        public static final String FAINT = "\033[2m";
        public static final String ITALIC = "\033[3m";
        public static final String UNDERLINE = "\033[4m";
        public static final String SLOW_BLINK = "\033[5m";

        public static final String NO_BOLD = "\033[22m";
        public static final String NO_ITALIC = "\033[23m";
        public static final String NO_UNDERLINE = "\033[24m";
        public static final String NO_BLINK = "\033[25m";

        public static final String RED = "\033[31m";
        public static final String GREEN = "\033[32m";
        public static final String YELLOW = "\033[33m";
        public static final String BLUE = "\033[34m";
        public static final String MAGENTA = "\033[35m";
        public static final String CYAN = "\033[36m";
        public static final String WHITE = "\033[37m";

        public static final String ORANGE = "\033[38;5;166m";
        public static final String DEFAULT_FG = "\033[39m";

        public static final String BRIGHT_BLACK = "\033[90m";
        public static final String BRIGHT_RED = "\033[91m";
        public static final String BRIGHT_GREEN = "\033[92m";
        public static final String BRIGHT_YELLOW = "\033[93m";
        public static final String BRIGHT_BLUE = "\033[94m";
        public static final String BRIGHT_MAGENTA = "\033[95m";
        public static final String BRIGHT_CYAN = "\033[96m";
        public static final String BRIGHT_WHITE = "\033[97m";

        public static final String CLEAR_SCREEN = "\033[2J\033[H";
        public static final String HIDE_CURSOR = "\033[?25l";
        public static final String SHOW_CURSOR = "\033[?25h";
    }

