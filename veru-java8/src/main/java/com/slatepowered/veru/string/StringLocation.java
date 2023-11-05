package com.slatepowered.veru.string;

/**
 * Represents a location in a string.
 *
 * It contains the (virtual) file name, the string
 * and the start and end indices.
 */
public class StringLocation implements StringLocatable {

    String file;
    AnalyzedString string;

    /* Both the start and end indices are inclusive. */
    int startIndex;
    int endIndex;

    /* The start and end line numbers. */
    int startLine;
    int endLine = -1;

    public StringLocation(String file, AnalyzedString str, int startIndex, int endIndex, int startLine) {
        this.file = file;
        this.string = str;
        this.startIndex = startIndex;
        this.endIndex = endIndex;
        this.startLine = startLine;
    }

    public StringLocation(String file, AnalyzedString str, int startIndex, int endIndex, int startLine, int endLine) {
        this.file = file;
        this.string = str;
        this.startIndex = startIndex;
        this.endIndex = endIndex;
        this.startLine = startLine;
        this.endLine = endLine;
    }

    /**
     * Get the file this location appears in.
     *
     * @return The file.
     */
    public String getFile() {
        return file;
    }

    /**
     * Get the analyzed string object for the source
     * of this location.
     *
     * @return The analyzed string.
     */
    public AnalyzedString getString() {
        return string;
    }

    public int getStartIndex() {
        return startIndex;
    }

    public int getEndIndex() {
        return endIndex;
    }

    public int getStartLineNumber() {
        return startLine;
    }

    public int getEndLineNumber() {
        if (endLine == -1) {
            // calculate
            endLine = startLine + Strings.countLines(string.getString(), startIndex, endIndex);
        }

        return endLine;
    }

    public int getStartColumn() {
        return startIndex - string.getLine(startLine).getStartIndex();
    }

    public int getEndColumn() {
        return endIndex - string.getLine(getEndLineNumber()).getEndIndex();
    }

    @Override
    public String toString() {
        return "StringLocation(" +
                "file='" + file + '\'' +
                ", string='" + string + '\'' +
                ", startIndex=" + startIndex +
                ", endIndex=" + endIndex +
                ')';
    }

    @Override
    public StringLocation getLocation() {
        return this;
    }

    @Override
    public StringLocation setLocation(StringLocation location) {
        this.startIndex = location.startIndex;
        this.endIndex = location.endIndex;
        this.file = location.file;
        this.string = location.string;
        return this;
    }

}