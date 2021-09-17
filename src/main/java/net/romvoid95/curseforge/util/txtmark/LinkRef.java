package net.romvoid95.curseforge.util.txtmark;

class LinkRef {
	public final String link;
	public String title;
	public final boolean isAbbrev;

	/**
	 * Constructor.
	 *
	 * @param link  The link.
	 * @param title The title (may be <code>null</code>).
	 */
	public LinkRef(final String link, final String title, final boolean isAbbrev) {
		this.link = link;
		this.title = title;
		this.isAbbrev = isAbbrev;
	}

	/** 
	 * @see java.lang.Object#toString() 
	 */
	@Override
	public String toString() {
		return this.link + " \"" + this.title + "\"";
	}
}
