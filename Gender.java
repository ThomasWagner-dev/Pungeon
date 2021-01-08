public enum Gender {
    F ("female", new String[]{"she", "her"}),
    M ("male", new String[]{"he", "him"}),
    NB("non-binary", new String[]{"they", "them"}),
    OTHER ("other", new String[] {"they","them"});

    public final String s;
    public String[] pronouns;
    Gender(String s, String[] pronouns) {
        this.s = s;
        this.pronouns = pronouns;
    }

    public static Gender from(String s) {
        for (Gender g : Gender.values()) {
            if (g.s.equals(s)) return g;
        }
        return OTHER;
    }
}
