package net.romvoid95.curseforge.util.txtmark;

import net.romvoid95.curseforge.util.txtmark.interfaces.Decorator;

public class DefaultDecorator implements Decorator {

	/**
	 * @see net.romvoid95.curseforge.util.txtmark.interfaces.Decorator#openParagraph(StringBuilder)
	 */
	@Override
	public void openParagraph(final StringBuilder out) {
		out.append("<p>");
	}

	/**
	 * @see net.romvoid95.curseforge.util.txtmark.interfaces.Decorator#closeParagraph(StringBuilder)
	 */
	@Override
	public void closeParagraph(final StringBuilder out) {
		out.append("</p>\n");
	}

	/**
	 * @see net.romvoid95.curseforge.util.txtmark.interfaces.Decorator#openBlockquote(StringBuilder)
	 */
	@Override
	public void openBlockquote(final StringBuilder out) {
		out.append("<blockquote>");
	}

	/**
	 * @see net.romvoid95.curseforge.util.txtmark.interfaces.Decorator#closeBlockquote(StringBuilder)
	 */
	@Override
	public void closeBlockquote(final StringBuilder out) {
		out.append("</blockquote>\n");
	}

	/**
	 * @see net.romvoid95.curseforge.util.txtmark.interfaces.Decorator#openCodeBlock(StringBuilder)
	 */
	@Override
	public void openCodeBlock(final StringBuilder out) {
		out.append("<pre><code>");
	}

	/**
	 * @see net.romvoid95.curseforge.util.txtmark.interfaces.Decorator#closeCodeBlock(StringBuilder)
	 */
	@Override
	public void closeCodeBlock(final StringBuilder out) {
		out.append("</code></pre>\n");
	}

	/**
	 * @see net.romvoid95.curseforge.util.txtmark.interfaces.Decorator#openCodeSpan(StringBuilder)
	 */
	@Override
	public void openCodeSpan(final StringBuilder out) {
		out.append("<code>");
	}

	/**
	 * @see net.romvoid95.curseforge.util.txtmark.interfaces.Decorator#closeCodeSpan(StringBuilder)
	 */
	@Override
	public void closeCodeSpan(final StringBuilder out) {
		out.append("</code>");
	}

	/**
	 * @see net.romvoid95.curseforge.util.txtmark.interfaces.Decorator#openHeadline(StringBuilder, int)
	 */
	@Override
	public void openHeadline(final StringBuilder out, final int level) {
		out.append("<h");
		out.append(level);
	}

	/**
	 * @see net.romvoid95.curseforge.util.txtmark.interfaces.Decorator#closeHeadline(StringBuilder, int)
	 */
	@Override
	public void closeHeadline(final StringBuilder out, final int level) {
		out.append("</h");
		out.append(level);
		out.append(">\n");
	}

	/**
	 * @see net.romvoid95.curseforge.util.txtmark.interfaces.Decorator#openStrong(StringBuilder)
	 */
	@Override
	public void openStrong(final StringBuilder out) {
		out.append("<strong>");
	}

	/**
	 * @see net.romvoid95.curseforge.util.txtmark.interfaces.Decorator#closeStrong(StringBuilder)
	 */
	@Override
	public void closeStrong(final StringBuilder out) {
		out.append("</strong>");
	}

	/**
	 * @see net.romvoid95.curseforge.util.txtmark.interfaces.Decorator#openEmphasis(StringBuilder)
	 */
	@Override
	public void openEmphasis(final StringBuilder out) {
		out.append("<em>");
	}

	/**
	 * @see net.romvoid95.curseforge.util.txtmark.interfaces.Decorator#closeEmphasis(StringBuilder)
	 */
	@Override
	public void closeEmphasis(final StringBuilder out) {
		out.append("</em>");
	}

	/**
	 * @see net.romvoid95.curseforge.util.txtmark.interfaces.Decorator#openSuper(StringBuilder)
	 */
	@Override
	public void openSuper(final StringBuilder out) {
		out.append("<sup>");
	}

	/**
	 * @see net.romvoid95.curseforge.util.txtmark.interfaces.Decorator#closeSuper(StringBuilder)
	 */
	@Override
	public void closeSuper(final StringBuilder out) {
		out.append("</sup>");
	}

	/**
	 * @see net.romvoid95.curseforge.util.txtmark.interfaces.Decorator#openOrderedList(StringBuilder)
	 */
	@Override
	public void openOrderedList(final StringBuilder out) {
		out.append("<ol>\n");
	}

	/**
	 * @see net.romvoid95.curseforge.util.txtmark.interfaces.Decorator#closeOrderedList(StringBuilder)
	 */
	@Override
	public void closeOrderedList(final StringBuilder out) {
		out.append("</ol>\n");
	}

	/**
	 * @see net.romvoid95.curseforge.util.txtmark.interfaces.Decorator#openUnorderedList(StringBuilder)
	 */
	@Override
	public void openUnorderedList(final StringBuilder out) {
		out.append("<ul>\n");
	}

	/**
	 * @see net.romvoid95.curseforge.util.txtmark.interfaces.Decorator#closeUnorderedList(StringBuilder)
	 */
	@Override
	public void closeUnorderedList(final StringBuilder out) {
		out.append("</ul>\n");
	}

	/**
	 * @see net.romvoid95.curseforge.util.txtmark.interfaces.Decorator#openListItem(StringBuilder)
	 */
	@Override
	public void openListItem(final StringBuilder out) {
		out.append("<li");
	}

	/**
	 * @see net.romvoid95.curseforge.util.txtmark.interfaces.Decorator#closeListItem(StringBuilder)
	 */
	@Override
	public void closeListItem(final StringBuilder out) {
		out.append("</li>\n");
	}

	/**
	 * @see net.romvoid95.curseforge.util.txtmark.interfaces.Decorator#horizontalRuler(StringBuilder)
	 */
	@Override
	public void horizontalRuler(final StringBuilder out) {
		out.append("<hr />\n");
	}

	/**
	 * @see net.romvoid95.curseforge.util.txtmark.interfaces.Decorator#openLink(StringBuilder)
	 */
	@Override
	public void openLink(final StringBuilder out) {
		out.append("<a");
	}

	/**
	 * @see net.romvoid95.curseforge.util.txtmark.interfaces.Decorator#closeLink(StringBuilder)
	 */
	@Override
	public void closeLink(final StringBuilder out) {
		out.append("</a>");
	}

	/**
	 * @see net.romvoid95.curseforge.util.txtmark.interfaces.Decorator#openImage(StringBuilder)
	 */
	@Override
	public void openImage(final StringBuilder out) {
		out.append("<img");
	}

	/**
	 * @see net.romvoid95.curseforge.util.txtmark.interfaces.Decorator#closeImage(StringBuilder)
	 */
	@Override
	public void closeImage(final StringBuilder out) {
		out.append(" />");
	}
}
