import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// WordType Interface - Part of Flyweight Pattern
interface WordType {
    String getType();
}

// Concrete WordType implementations
class Verb implements WordType {
    public String getType() {
        return "verb";
    }
}

class Noun implements WordType {
    public String getType() {
        return "noun";
    }
}

class Adjective implements WordType {
    public String getType() {
        return "adjective";
    }
}

// WordStyle Interface - Part of Flyweight Pattern
interface WordStyle {
    String getStyle();
}

// Concrete WordStyle implementations
class Bold implements WordStyle {
    public String getStyle() {
        return "bold";
    }
}

class Italic implements WordStyle {
    public String getStyle() {
        return "italic";
    }
}

class Underline implements WordStyle {
    public String getStyle() {
        return "underline";
    }
}

// WordTypeFactory - Factory for creating WordType instances
class WordTypeFactory {
    private static final Map<String, WordType> typePool = new HashMap<>();

    public static WordType getWordType(String type) {
        WordType wordType = typePool.get(type);

        if (wordType == null) {
            switch (type) {
                case "verb":
                    wordType = new Verb();
                    break;
                case "noun":
                    wordType = new Noun();
                    break;
                case "adjective":
                    wordType = new Adjective();
                    break;
                default:
                    throw new IllegalArgumentException("Unknown word type: " + type);
            }
            typePool.put(type, wordType);
        }

        return wordType;
    }
}

// WordStyleFactory - Factory for creating WordStyle instances
class WordStyleFactory {
    private static final Map<String, WordStyle> stylePool = new HashMap<>();

    public static WordStyle getWordStyle(String style) {
        WordStyle wordStyle = stylePool.get(style);

        if (wordStyle == null) {
            switch (style) {
                case "bold":
                    wordStyle = new Bold();
                    break;
                case "italic":
                    wordStyle = new Italic();
                    break;
                case "underline":
                    wordStyle = new Underline();
                    break;
                default:
                    throw new IllegalArgumentException("Unknown word style: " + style);
            }
            stylePool.put(style, wordStyle);
        }

        return wordStyle;
    }
}

// Word class - Uses flyweight pattern for WordType and WordStyle
class Word {
    private String text;
    private int position;
    private WordType wordType;
    private WordStyle wordStyle;

    public Word(String text, int position, String type, String style) {
        this.text = text;
        this.position = position;
        this.wordType = WordTypeFactory.getWordType(type);
        this.wordStyle = WordStyleFactory.getWordStyle(style);
    }

    public String getText() {
        return text;
    }

    public int getPosition() {
        return position;
    }

    public WordType getWordType() {
        return wordType;
    }

    public WordStyle getWordStyle() {
        return wordStyle;
    }

    public String toString() {
        return "Word['" + text + "', position=" + position +
                ", type=" + wordType.getType() + ", style=" + wordStyle.getStyle() + "]";
    }
}

// TextFormatter - Chain of Responsibility Pattern
abstract class TextFormatter {
    private TextFormatter nextFormatter;

    public void setNext(TextFormatter nextFormatter) {
        this.nextFormatter = nextFormatter;
    }

    public String format(Word word) {
        if (canFormat(word)) {
            return applyFormat(word.getText());
        } else if (nextFormatter != null) {
            return nextFormatter.format(word);
        } else {
            return word.getText(); // No formatting applied
        }
    }

    protected abstract boolean canFormat(Word word);

    protected abstract String applyFormat(String text);
}

// Concrete TextFormatter implementations
class BoldFormatter extends TextFormatter {
    @Override
    protected boolean canFormat(Word word) {
        return word.getWordStyle().getStyle().equals("bold");
    }

    @Override
    protected String applyFormat(String text) {
        return "<b>" + text + "</b>";
    }
}

class ItalicFormatter extends TextFormatter {
    @Override
    protected boolean canFormat(Word word) {
        return word.getWordStyle().getStyle().equals("italic");
    }

    @Override
    protected String applyFormat(String text) {
        return "<i>" + text + "</i>";
    }
}

class UnderlineFormatter extends TextFormatter {
    @Override
    protected boolean canFormat(Word word) {
        return word.getWordStyle().getStyle().equals("underline");
    }

    @Override
    protected String applyFormat(String text) {
        return "<u>" + text + "</u>";
    }
}

// WordProcessor - Client class that uses formatters
class WordProcessor {
    private TextFormatter formatterChain;

    public WordProcessor() {
        // Set up the chain of responsibility
        BoldFormatter boldFormatter = new BoldFormatter();
        ItalicFormatter italicFormatter = new ItalicFormatter();
        UnderlineFormatter underlineFormatter = new UnderlineFormatter();

        boldFormatter.setNext(italicFormatter);
        italicFormatter.setNext(underlineFormatter);

        formatterChain = boldFormatter;
    }

    public String process(List<Word> words) {
        // Sort words by position
        words.sort(Comparator.comparingInt(Word::getPosition));

        StringBuilder result = new StringBuilder();
        for (Word word : words) {
            String formatted = formatterChain.format(word);
            result.append(formatted).append(" ");
        }

        return result.toString().trim();
    }
}

// Main class for testing
public class WordProcessingSystem {
    public static void main(String[] args) {
        // Create words with different types and styles
        List<Word> words = new ArrayList<>();
        words.add(new Word("The", 0, "adjective", "bold"));
        words.add(new Word("slow", 1, "adjective", "italic"));
        words.add(new Word("black", 2, "adjective", "underline"));
        words.add(new Word("rabbit", 3, "noun", "bold"));
        words.add(new Word("jumps", 4, "verb", "italic"));
        words.add(new Word("over", 5, "adjective", "underline"));
        words.add(new Word("lazy", 7, "adjective", "bold"));
        words.add(new Word("cat", 8, "noun", "italic"));

        // Process the words
        WordProcessor processor = new WordProcessor();
        String result = processor.process(words);

        System.out.println("Processed sentence: " + result);

        // Demonstrate adding a new word and processing again
        words.add(new Word("the", 6, "adjective", "underline"));
        result = processor.process(words);

        System.out.println("\nUpdated sentence: " + result);
    }
}