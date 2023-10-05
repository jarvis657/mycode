package argrith.strings;

public class Word {
    public int lengthOfLastWord(String s) {
        int len = s.length();
        int j = 0;
        for (int i = len - 1; i >= 0; i--) {
            if (j == 0 && s.charAt(i) == ' ') {
                continue;
            } else if (j > 0 && s.charAt(i) == ' ') {
                break;
            }
            j++;
        }
        return j;
    }

    public static void main(String[] args) {
        Word word = new Word();
       System.out.println(word.lengthOfLastWord("hello world"));
    }
}
