package com.example.demo.tools;

/**
 * 文本差异
 * 
 * @author phy
 * 
 */
public class TextDiff {
	public TextDiff(int diffType, int diffStartIndex, int diffLength) {
		super();
		this.setDiffType(diffType);
		this.diffStartIndex = diffStartIndex;
		this.setDiffLength(diffLength);
	}

	public TextDiff() {
		super();
	}

	public static final int TYPE_DELETED = 0x102;// 删除字符
	public static final int TYPE_INSERTED = 0x103;// 新添加字符
	private int diffType;// 差异的类型，比如删除或插入

	private int diffStartIndex;// 差异的开始点
	private int diffLength;// 差异的长度

	public int getDiffType() {
		return diffType;
	}

	public void setDiffType(int diffType) {
		if (diffType != TYPE_DELETED && diffType != TYPE_INSERTED)
			throw new IllegalArgumentException("未知文本差异类型[" + diffType
					+ "]，差异类型示例：TextDiff.TYPE_DELETED");
		this.diffType = diffType;
	}

	public int getDiffStartIndex() {
		return diffStartIndex;
	}

	public void setDiffStartIndex(int diffStartIndex) {
		this.diffStartIndex = diffStartIndex;
	}

	public void setDiffLength(int diffLength) {
		this.diffLength = diffLength;
	}

	public int getDiffLength() {
		return diffLength;
	}

	public String toString() {
		return (diffType == TYPE_DELETED ? "删除" : "新增") + ": 从"
				+ diffStartIndex + "到" + (diffStartIndex + diffLength);
	}
}