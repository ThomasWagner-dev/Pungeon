/**
 * Gender is a bad social construct (ge<b>schlecht</b>). It makes thausands of people depressed and make some even commit suicide.
 * Stop using gender.
 */
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

    public static Gender fromPronouns(String pronouns) {
        return fromPronouns(pronouns.split("[/;,]"));
    }

    public static Gender fromPronouns(String[] pronouns) {
        for(Gender g : Gender.values()) {
            if (g.pronouns[0].equals(pronouns[0]) && g.pronouns[1].equals(pronouns[1])) {
                return g;
            }
        }
        return OTHER;
    }
}
