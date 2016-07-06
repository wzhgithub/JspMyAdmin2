/**
 * 
 */
package com.jspmyadmin.framework.tag.jma;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;

import com.jspmyadmin.framework.tag.support.AbstractSimpleTagSupport;
import com.jspmyadmin.framework.util.FrameworkConstants;

/**
 * 
 * @author Yugandhar Gangu
 * @created_at 2016/06/28
 *
 */
public class FetchTag extends AbstractSimpleTagSupport {

	private String name = null;
	private String index = null;
	private String key = null;

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @param index
	 *            the index to set
	 */
	public void setIndex(String index) {
		this.index = index;
	}

	/**
	 * @param key
	 *            the key to set
	 */
	public void setKey(String key) {
		this.key = key;
	}

	@Override
	public void doTag() throws JspException, IOException {
		PageContext pageContext = (PageContext) super.getJspContext();
		if (name == null || index == null) {
			return;
		}
		int indexVal = -1;
		if (index.startsWith(FrameworkConstants.SYMBOL_HASH)) {
			index = index.substring(1);
			Object temp = pageContext.getAttribute(index);
			if (temp == null) {
				return;
			} else {
				if (temp instanceof Integer) {
					indexVal = (Integer) temp;
				} else if (isInteger(temp.toString())) {
					indexVal = Integer.parseInt(temp.toString());
				}
			}
		} else if (isInteger(index)) {
			indexVal = Integer.parseInt(index);
		}
		if (indexVal < 0) {
			return;
		}
		Object temp = pageContext.getRequest().getAttribute(FrameworkConstants.COMMAND);
		temp = super.getReflectValue(temp, name, indexVal);
		if (temp == null) {
			pageContext.setAttribute(key, null, PageContext.PAGE_SCOPE);
			return;
		}
		if (key == null || FrameworkConstants.BLANK.equals(key.trim())) {
			JspWriter jspWriter = pageContext.getOut();
			jspWriter.write(temp.toString());
		} else {
			pageContext.setAttribute(key, temp, PageContext.PAGE_SCOPE);
		}
	}

	/**
	 * 
	 * @param val
	 * @return
	 */
	private boolean isInteger(String val) {
		try {
			if (val != null) {
				Integer.parseInt(val);
				return true;
			}
		} catch (Exception e) {
		}
		return false;
	}
}
