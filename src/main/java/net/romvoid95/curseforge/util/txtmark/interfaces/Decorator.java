package net.romvoid95.curseforge.util.txtmark.interfaces;

public interface Decorator
{
    /**
     * Called when a paragraph is opened.
     *
     * <p>
     * Default implementation is:
     * </p>
     *
     * <pre>
     * <code>out.append("&lt;p&gt;");</code>
     * </pre>
     *
     * @param out
     *            The StringBuilder to write to.
     */
    public void openParagraph(final StringBuilder out);

    /**
     * Called when a paragraph is closed.
     *
     * <p>
     * Default implementation is:
     * </p>
     *
     * <pre>
     * <code>out.append("&lt;/p&gt;\n");</code>
     * </pre>
     *
     * @param out
     *            The StringBuilder to write to.
     */
    public void closeParagraph(final StringBuilder out);

    /**
     * Called when a blockquote is opened.
     *
     * Default implementation is:
     *
     * <pre>
     * <code>out.append("&lt;blockquote&gt;");</code>
     * </pre>
     *
     * @param out
     *            The StringBuilder to write to.
     */
    public void openBlockquote(final StringBuilder out);

    /**
     * Called when a blockquote is closed.
     *
     * <p>
     * Default implementation is:
     * </p>
     *
     * <pre>
     * <code>out.append("&lt;/blockquote&gt;\n");</code>
     * </pre>
     *
     * @param out
     *            The StringBuilder to write to.
     */
    public void closeBlockquote(final StringBuilder out);

    /**
     * Called when a code block is opened.
     *
     * <p>
     * Default implementation is:
     * </p>
     *
     * <pre>
     * <code>out.append("&lt;pre&gt;&lt;code&gt;");</code>
     * </pre>
     *
     * @param out
     *            The StringBuilder to write to.
     */
    public void openCodeBlock(final StringBuilder out);

    /**
     * Called when a code block is closed.
     *
     * <p>
     * Default implementation is:
     * </p>
     *
     * <pre>
     * <code>out.append("&lt;/code&gt;&lt;/pre&gt;\n");</code>
     * </pre>
     *
     * @param out
     *            The StringBuilder to write to.
     */
    public void closeCodeBlock(final StringBuilder out);

    /**
     * Called when a code span is opened.
     *
     * <p>
     * Default implementation is:
     * </p>
     *
     * <pre>
     * <code>out.append("&lt;code&gt;");</code>
     * </pre>
     *
     * @param out
     *            The StringBuilder to write to.
     */
    public void openCodeSpan(final StringBuilder out);

    /**
     * Called when a code span is closed.
     *
     * <p>
     * Default implementation is:
     * </p>
     *
     * <pre>
     * <code>out.append("&lt;/code&gt;");</code>
     * </pre>
     *
     * @param out
     *            The StringBuilder to write to.
     */
    public void closeCodeSpan(final StringBuilder out);

    /**
     * Called when a headline is opened.
     *
     * <p>
     * <strong>Note:</strong> Don't close the HTML tag!
     * </p>
     * <p>
     * Default implementation is:
     * </p>
     *
     * <pre>
     * <code> out.append("&lt;h");
     * out.append(level);</code>
     * </pre>
     *
     * @param out
     *            The StringBuilder to write to.
     * @param level
     *            The level to use.
     */
    public void openHeadline(final StringBuilder out, int level);

    /**
     * Called when a headline is closed.
     *
     * <p>
     * Default implementation is:
     * </p>
     *
     * <pre>
     * <code> out.append("&lt;/h");
     * out.append(level);
     * out.append("&gt;\n");</code>
     * </pre>
     *
     * @param out
     *            The StringBuilder to write to.
     * @param level
     *            The level to use.
     */
    public void closeHeadline(final StringBuilder out, int level);

    /**
     * Called when a strong span is opened.
     *
     * <p>
     * Default implementation is:
     * </p>
     *
     * <pre>
     * <code>out.append("&lt;strong&gt;");</code>
     * </pre>
     *
     * @param out
     *            The StringBuilder to write to.
     */
    public void openStrong(final StringBuilder out);

    /**
     * Called when a strong span is closed.
     *
     * <p>
     * Default implementation is:
     * </p>
     *
     * <pre>
     * <code>out.append("&lt;/strong&gt;");</code>
     * </pre>
     *
     * @param out
     *            The StringBuilder to write to.
     */
    public void closeStrong(final StringBuilder out);

    /**
     * Called when an emphasis span is opened.
     *
     * <p>
     * Default implementation is:
     * </p>
     *
     * <pre>
     * <code>out.append("&lt;em&gt;");</code>
     * </pre>
     *
     * @param out
     *            The StringBuilder to write to.
     */
    public void openEmphasis(final StringBuilder out);

    /**
     * Called when an emphasis span is closed.
     *
     * <p>
     * Default implementation is:
     * </p>
     *
     * <pre>
     * <code>out.append("&lt;/em&gt;");</code>
     * </pre>
     *
     * @param out
     *            The StringBuilder to write to.
     */
    public void closeEmphasis(final StringBuilder out);

    /**
     * Called when a superscript span is opened.
     *
     * <p>
     * Default implementation is:
     * </p>
     *
     * <pre>
     * <code>out.append("&lt;sup&gt;");</code>
     * </pre>
     *
     * @param out
     *            The StringBuilder to write to.
     */
    public void openSuper(final StringBuilder out);

    /**
     * Called when a superscript span is closed.
     *
     * <p>
     * Default implementation is:
     * </p>
     *
     * <pre>
     * <code>out.append("&lt;/sup&gt;");</code>
     * </pre>
     *
     * @param out
     *            The StringBuilder to write to.
     */
    public void closeSuper(final StringBuilder out);

    /**
     * Called when an ordered list is opened.
     *
     * <p>
     * Default implementation is:
     * </p>
     *
     * <pre>
     * <code>out.append("&lt;ol&gt;\n");</code>
     * </pre>
     *
     * @param out
     *            The StringBuilder to write to.
     */
    public void openOrderedList(final StringBuilder out);

    /**
     * Called when an ordered list is closed.
     *
     * <p>
     * Default implementation is:
     * </p>
     *
     * <pre>
     * <code>out.append("&lt;/ol&gt;\n");</code>
     * </pre>
     *
     * @param out
     *            The StringBuilder to write to.
     */
    public void closeOrderedList(final StringBuilder out);

    /**
     * Called when an unordered list is opened.
     *
     * <p>
     * Default implementation is:
     * </p>
     *
     * <pre>
     * <code>out.append("&lt;ul&gt;\n");</code>
     * </pre>
     *
     * @param out
     *            The StringBuilder to write to.
     */
    public void openUnorderedList(final StringBuilder out);

    /**
     * Called when an unordered list is closed.
     *
     * <p>
     * Default implementation is:
     * </p>
     *
     * <pre>
     * <code>out.append("&lt;/ul&gt;\n");</code>
     * </pre>
     *
     * @param out
     *            The StringBuilder to write to.
     */
    public void closeUnorderedList(final StringBuilder out);

    /**
     * Called when a list item is opened.
     *
     * <p>
     * <strong>Note:</strong> Don't close the HTML tag!
     * </p>
     * <p>
     * Default implementation is:
     * </p>
     *
     * <pre>
     * <code>out.append("&lt;li");</code>
     * </pre>
     *
     * @param out
     *            The StringBuilder to write to.
     */
    public void openListItem(final StringBuilder out);

    /**
     * Called when a list item is closed.
     *
     * <p>
     * Default implementation is:
     * </p>
     *
     * <pre>
     * <code>out.append("&lt;/li&gt;\n");</code>
     * </pre>
     *
     * @param out
     *            The StringBuilder to write to.
     */
    public void closeListItem(final StringBuilder out);

    /**
     * Called when a horizontal ruler is encountered.
     *
     * <p>
     * Default implementation is:
     * </p>
     *
     * <pre>
     * <code>out.append("&lt;hr /&gt;\n");</code>
     * </pre>
     *
     * @param out
     *            The StringBuilder to write to.
     */
    public void horizontalRuler(final StringBuilder out);

    /**
     * Called when a link is opened.
     *
     * <p>
     * <strong>Note:</strong> Don't close the HTML tag!
     * </p>
     * <p>
     * Default implementation is:
     * </p>
     *
     * <pre>
     * <code>out.append("&lt;a");</code>
     * </pre>
     *
     * @param out
     *            The StringBuilder to write to.
     */
    public void openLink(final StringBuilder out);

    /**
     * Called when a link is closed
     *
     * <p>
     * Default implementation is:
     * </p>
     *
     * <pre>
     * <code>out.append("&lt;/a&gt;");</code>
     * </pre>
     *
     * @param out
     *            The StringBuilder to write to.
     */
    public void closeLink(final StringBuilder out);

    /**
     * Called when an image is opened.
     *
     * <p>
     * <strong>Note:</strong> Don't close the HTML tag!
     * </p>
     * <p>
     * Default implementation is:
     * </p>
     *
     * <pre>
     * <code>out.append("&lt;img");</code>
     * </pre>
     *
     * @param out
     *            The StringBuilder to write to.
     */
    public void openImage(final StringBuilder out);

    /**
     * Called when an image is closed.
     *
     * <p>
     * Default implementation is:
     * </p>
     *
     * <pre>
     * <code>out.append(" /&gt;");</code>
     * </pre>
     *
     * @param out
     *            The StringBuilder to write to.
     */
    public void closeImage(final StringBuilder out);
}
