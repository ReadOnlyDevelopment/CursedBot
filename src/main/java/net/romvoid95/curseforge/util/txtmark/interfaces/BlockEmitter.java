package net.romvoid95.curseforge.util.txtmark.interfaces;

import java.util.List;

public interface BlockEmitter
{
    /**
     * This method is responsible for outputting a markdown block and for any
     * needed pre-processing like escaping HTML special characters.
     *
     * @param out
     *            The StringBuilder to append to
     * @param lines
     *            List of lines
     * @param meta
     *            Meta information as a single String (if any) or empty String
     */
    public void emitBlock(StringBuilder out, List<String> lines, String meta);
}
