package net.romvoid95.curseforge.util.txtmark.enums;

/**
 * Markdown token enumeration.
 */
public enum MarkToken {
	/**
	 * No token.
	 */
	NONE,
	/**
	 * &#x2a;
	 */
	EM_STAR,
	/**
	 * _
	 */
	EM_UNDERSCORE,
	/**
	 * &#x2a;&#x2a;
	 */
	STRONG_STAR,
	/**
	 * __
	 */
	STRONG_UNDERSCORE,
	/**
	 * `
	 */
	CODE_SINGLE,
	/**
	 * ``
	 */
	CODE_DOUBLE,
	/**
	 * [
	 */
	LINK,
	/**
	 * &lt;
	 */
	HTML,
	/**
	 * ![
	 */
	IMAGE,
	/**
	 * &amp;
	 */
	ENTITY,
	/**
	 * \
	 */
	ESCAPE,
	/**
	 * Extended: ^
	 */
	SUPER,
	/**
	 * Extended: (C)
	 */
	X_COPY,
	/**
	 * Extended: (R)
	 */
	X_REG,
	/**
	 * Extended: (TM)
	 */
	X_TRADE,
	/**
	 * Extended: &lt;&lt;
	 */
	X_LAQUO,
	/**
	 * Extended: >>
	 */
	X_RAQUO,
	/**
	 * Extended: --
	 */
	X_NDASH,
	/**
	 * Extended: ---
	 */
	X_MDASH,
	/**
	 * Extended: &#46;&#46;&#46;
	 */
	X_HELLIP,
	/**
	 * Extended: "x
	 */
	X_RDQUO,
	/**
	 * Extended: x"
	 */
	X_LDQUO,
	/**
	 * [[
	 */
	X_LINK_OPEN,
	/**
	 * ]]
	 */
	X_LINK_CLOSE,
}
