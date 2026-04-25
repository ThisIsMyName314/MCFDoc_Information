package net.justgnat.mcfdoc.sanitiser;

public enum TagType {

    /*
    The <br> tag is used to represent a line break
     */
    BR,

    /*
    The <strong> tag is used to bolden a section
     */
    STRONG,

    /*
    The <i> tag is used to make a section italicised
     */
    I,

    /*
    The <small> tag is used to make a section smaller in font size
     */
    SMALL,

    /*
    The <big> tag is used to make a section larger in font size
     */
    BIG,

    /*
    The <ins> tag is used to underline a section
     */
    INS,

    /*
    The <del> tag is used to add a strikethrough to a section
     */
    DEL,

    /*
    This tag indicates that it is not safe for MCF Docs
     */
    DISALLOWED

}
