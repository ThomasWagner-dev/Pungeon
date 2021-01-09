public enum Gender {
    F ("female", new String[]{"she", "her"}),
    M ("male", new String[]{"he", "him"}),
    NB("non-binary", new String[]{"they", "them"}),
    OTHER ("other", new String[] {"they","them"});

    public final String s;
    public String[] pronouns;

    /**
     * Gender is just a social construct
     * @param s gender label
     * @param pronouns the pronouns people with such label use
     */
    Gender(String s, String[] pronouns) {
        this.s = s;
        this.pronouns = pronouns;
    }

    /**
     * fetches a gender from a gender label
     * @param s gender label
     * @return the according gender
     */
    public static Gender from(String s) {
        for (Gender g : Gender.values()) {
            if (g.s.equals(s)) return g;
        }
        return OTHER;
    }
}
