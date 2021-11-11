package net.romvoid95.curseforge.util.builder;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.google.common.base.Supplier;

public abstract class DataClass<T> {
	
	private T objectClass;

	@Override
	public String toString() {
		ToStringStyle style = new MultiLineStyle();
		ToStringBuilder builder = new ToStringBuilder(objectClass, style);
		return builder.build();
	}
	
	private final class MultiLineStyle extends ToStringStyle {

		private static final long serialVersionUID = 1L;

		MultiLineStyle() {
            this.setContentStart("{");
            this.setUseShortClassName(true);
            this.setArrayStart("[");
            this.setArrayEnd("]");
            this.setDefaultFullDetail(true);
            this.setFieldSeparator(System.lineSeparator() + "  ");
            this.setFieldSeparatorAtStart(true);
            this.setContentEnd(System.lineSeparator() + "}");
        }
		
        private Object readResolve() {
            return MULTI_LINE_STYLE;
        }
	}
}
