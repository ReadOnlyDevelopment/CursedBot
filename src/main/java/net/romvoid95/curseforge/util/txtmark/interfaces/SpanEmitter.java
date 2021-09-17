package net.romvoid95.curseforge.util.txtmark.interfaces;

public interface SpanEmitter
{
    /**
     * Emits a span element.
     *
     * @param out
     *            The StringBuilder to append to.
     * @param content
     *            The span's content.
     */
    public void emitSpan(StringBuilder out, String content);
}
