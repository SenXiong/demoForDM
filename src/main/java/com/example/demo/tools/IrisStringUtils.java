package com.example.demo.tools;

import java.io.StringReader;
import java.math.BigDecimal;
import java.sql.Clob;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.rtf.RTFEditorKit;

import org.apache.commons.lang.StringUtils;

import com.example.demo.exception.ServiceException;

/**
 * Miscellaneous string utility methods. Mainly for internal use within the framework; consider Jakarta's Commons Lang
 * for a more comprehensive suite of string utilities.
 * 
 * <p>
 * This class delivers some simple functionality that should really be provided by the core Java String and StringBuffer
 * classes, such as the ability to replace all occurrences of a given substring in a target string. It also provides
 * easy-to-use methods to convert between delimited strings, such as CSV strings, and collections and arrays.
 * 
 * <p>
 * 该程序是基于Spring2.0的工具类org.springframework.util.StringUtils的，大部分的功能都是
 * Spring中的实现。但是原来的方法中的替换(replace)，分割(split)等方法都是没有基于正则表达式， 使得他的功能很弱。所以我对此类方法进行了增强，并且保留原来的方法，如果想使用增强的方法，一般的
 * 命名方式是在原用方法名后面加上ByPattern，例如replaceByPattern,splitByPattern。
 * 
 * <p>
 * 并且在该类中增加了更多的方法，其中有中文注释的方法则为Tom Koo添加。
 * 
 * @author Rod Johnson
 * @author Juergen Hoeller
 * @author Keith Donald
 * @author Rob Harrop
 * @author Tom Koo
 * @since 16 April 2001
 * @see StringUtils
 */
@SuppressWarnings("unchecked")
public class IrisStringUtils {

	public static final String PASSWOARD = "[[a-z]|[A-Z]|[0-9]]*";

	public static final String USERNAME = "[[a-z]|[A-Z]|[0-9]]*";

	public static final String NUMBER = "[0-9]*";

	public static final String LETTER = "[[A-Z]|[a-z]]*";

	public static final String PHONE_NO = "0[0-9]{2,3}-[0-9]{7,8}";

	public static final String MOBILE_PHONE_NO = "13[0-9][0-9]{8}";

	private static final String FOLDER_SEPARATOR = "/";

	private static final String WINDOWS_FOLDER_SEPARATOR = "\\";

	private static final String TOP_PATH = "..";

	private static final String CURRENT_PATH = ".";

	private static final char EXTENSION_SEPARATOR = '.';

	// ---------------------------------------------------------------------
	// General convenience methods for working with Strings
	// ---------------------------------------------------------------------

	/**
	 * Check that the given String is neither <code>null</code> nor of length 0. Note: Will return <code>true</code> for
	 * a String that purely consists of whitespace.
	 * <p>
	 * 
	 * <pre>
	 *                                                                                                StringUtils.hasLength(null) = false
	 *                                                                                                StringUtils.hasLength(&quot;&quot;) = false
	 *                                                                                                StringUtils.hasLength(&quot; &quot;) = true
	 *                                                                                                StringUtils.hasLength(&quot;Hello&quot;) = true
	 * </pre>
	 * 
	 * @param str
	 *            the String to check (may be <code>null</code>)
	 * @return <code>true</code> if the String is not null and has length
	 * @see #hasText(String)
	 */
	public static boolean hasLength(String str) {
		return str != null && str.length() > 0;
	}

	/**
	 * Check whether the given String has actual text. More specifically, returns <code>true</code> if the string not
	 * <code>null<code>,
	 * its length is greater than 0, and it contains at least one non-whitespace character.
	 * <p><pre>
	 *                                                                                                StringUtils.hasText(null) = false
	 *                                                                                                StringUtils.hasText(&quot;&quot;) = false
	 *                                                                                                StringUtils.hasText(&quot; &quot;) = false
	 *                                                                                                StringUtils.hasText(&quot;12345&quot;) = true
	 *                                                                                                StringUtils.hasText(&quot; 12345 &quot;) = true
	 * </pre>
	 * 
	 * @param str
	 *            the String to check (may be <code>null</code>)
	 * @return <code>true</code> if the String is not <code>null</code>, its length is greater than 0, and is does not
	 *         contain whitespace only
	 * @see Character#isWhitespace
	 */
	public static boolean hasText(String str) {
		if (!hasLength(str)) {
			return false;
		}
		int strLen = str.length();
		for (int i = 0; i < strLen; i++) {
			if (!Character.isWhitespace(str.charAt(i))) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Check whether the given String contains any whitespace characters.
	 * 
	 * @param str
	 *            the String to check (may be <code>null</code>)
	 * @return <code>true</code> if the String is not empty and contains at least 1 whitespace character
	 * @see Character#isWhitespace
	 */
	public static boolean containsWhitespace(String str) {
		if (!hasLength(str)) {
			return false;
		}
		int strLen = str.length();
		for (int i = 0; i < strLen; i++) {
			if (Character.isWhitespace(str.charAt(i))) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Trim leading and trailing whitespace from the given String.
	 * 
	 * @param str
	 *            the String to check
	 * @return the trimmed String
	 * @see Character#isWhitespace
	 */
	public static String trimWhitespace(String str) {
		if (!hasLength(str)) {
			return str;
		}
		StringBuffer buf = new StringBuffer(str);
		while (buf.length() > 0 && Character.isWhitespace(buf.charAt(0))) {
			buf.deleteCharAt(0);
		}
		while (buf.length() > 0 && Character.isWhitespace(buf.charAt(buf.length() - 1))) {
			buf.deleteCharAt(buf.length() - 1);
		}
		return buf.toString();
	}

	/**
	 * Trim leading whitespace from the given String.
	 * 
	 * @param str
	 *            the String to check
	 * @return the trimmed String
	 * @see Character#isWhitespace
	 */
	public static String trimLeadingWhitespace(String str) {
		if (!hasLength(str)) {
			return str;
		}
		StringBuffer buf = new StringBuffer(str);
		while (buf.length() > 0 && Character.isWhitespace(buf.charAt(0))) {
			buf.deleteCharAt(0);
		}
		return buf.toString();
	}

	/**
	 * Trim trailing whitespace from the given String.
	 * 
	 * @param str
	 *            the String to check
	 * @return the trimmed String
	 * @see Character#isWhitespace
	 */
	public static String trimTrailingWhitespace(String str) {
		if (!hasLength(str)) {
			return str;
		}
		StringBuffer buf = new StringBuffer(str);
		while (buf.length() > 0 && Character.isWhitespace(buf.charAt(buf.length() - 1))) {
			buf.deleteCharAt(buf.length() - 1);
		}
		return buf.toString();
	}

	/**
	 * Trim <i>all</i> whitespace from the given String: leading, trailing, and inbetween characters.
	 * 
	 * @param str
	 *            the String to check
	 * @return the trimmed String
	 * @see Character#isWhitespace
	 */
	public static String trimAllWhitespace(String str) {
		if (!hasLength(str)) {
			return str;
		}
		StringBuffer buf = new StringBuffer(str);
		int index = 0;
		while (buf.length() > index) {
			if (Character.isWhitespace(buf.charAt(index))) {
				buf.deleteCharAt(index);
			} else {
				index++;
			}
		}
		return buf.toString();
	}

	/**
	 * Test if the given String starts with the specified prefix, ignoring upper/lower case.
	 * 
	 * @param str
	 *            the String to check
	 * @param prefix
	 *            the prefix to look for
	 * @see String#startsWith
	 */
	public static boolean startsWithIgnoreCase(String str, String prefix) {
		if (str == null || prefix == null) {
			return false;
		}
		if (str.startsWith(prefix)) {
			return true;
		}
		if (str.length() < prefix.length()) {
			return false;
		}
		String lcStr = str.substring(0, prefix.length()).toLowerCase();
		String lcPrefix = prefix.toLowerCase();
		return lcStr.equals(lcPrefix);
	}

	/**
	 * Test if the given String ends with the specified suffix, ignoring upper/lower case.
	 * 
	 * @param str
	 *            the String to check
	 * @param suffix
	 *            the suffix to look for
	 * @see String#endsWith
	 */
	public static boolean endsWithIgnoreCase(String str, String suffix) {
		if (str == null || suffix == null) {
			return false;
		}
		if (str.endsWith(suffix)) {
			return true;
		}
		if (str.length() < suffix.length()) {
			return false;
		}

		String lcStr = str.substring(str.length() - suffix.length()).toLowerCase();
		String lcSuffix = suffix.toLowerCase();
		return lcStr.equals(lcSuffix);
	}

	/**
	 * Count the occurrences of the substring in string s.
	 * 
	 * @param str
	 *            string to search in. Return 0 if this is null.
	 * @param sub
	 *            string to search for. Return 0 if this is null.
	 */
	public static int countOccurrencesOf(String str, String sub) {
		if (str == null || sub == null || str.length() == 0 || sub.length() == 0) {
			return 0;
		}
		int count = 0, pos = 0, idx = 0;
		while ((idx = str.indexOf(sub, pos)) != -1) {
			++count;
			pos = idx + sub.length();
		}
		return count;
	}

	/**
	 * Replace all occurences of a substring within a string with another string.
	 * 
	 * @param inString
	 *            String to examine
	 * @param oldPattern
	 *            String to replace
	 * @param newPattern
	 *            String to insert
	 * @return a String with the replacements
	 */
	public static String replace(String inString, String oldPattern, String newPattern) {
		if (inString == null) {
			return null;
		}
		if (oldPattern == null || newPattern == null) {
			return inString;
		}

		StringBuffer sbuf = new StringBuffer();
		// output StringBuffer we'll build up
		int pos = 0; // our position in the old string
		int index = inString.indexOf(oldPattern);
		// the index of an occurrence we've found, or -1
		int patLen = oldPattern.length();
		while (index >= 0) {
			sbuf.append(inString.substring(pos, index));
			sbuf.append(newPattern);
			pos = index + patLen;
			index = inString.indexOf(oldPattern, pos);
		}
		sbuf.append(inString.substring(pos));

		// remember to append any characters to the right of a match
		return sbuf.toString();
	}

	/**
	 * Delete all occurrences of the given substring.
	 * 
	 * @param pattern
	 *            the pattern to delete all occurrences of
	 */
	public static String delete(String inString, String pattern) {
		return replace(inString, pattern, "");
	}

	/**
	 * Delete any character in a given string.
	 * 
	 * @param charsToDelete
	 *            a set of characters to delete. E.g. "az\n" will delete 'a's, 'z's and new lines.
	 */
	public static String deleteAny(String inString, String charsToDelete) {
		if (inString == null || charsToDelete == null) {
			return inString;
		}
		StringBuffer out = new StringBuffer();
		for (int i = 0; i < inString.length(); i++) {
			char c = inString.charAt(i);
			if (charsToDelete.indexOf(c) == -1) {
				out.append(c);
			}
		}
		return out.toString();
	}

	// ---------------------------------------------------------------------
	// Convenience methods for working with formatted Strings
	// ---------------------------------------------------------------------

	/**
	 * Quote the given String with single quotes.
	 * 
	 * @param str
	 *            the input String (e.g. "myString")
	 * @return the quoted String (e.g. "'myString'"), or <code>null<code> if the input was <code>null</code>
	 */
	public static String quote(String str) {
		return str != null ? "'" + str + "'" : null;
	}

	/**
	 * Turn the given Object into a String with single quotes if it is a String; keeping the Object as-is else.
	 * 
	 * @param obj
	 *            the input Object (e.g. "myString")
	 * @return the quoted String (e.g. "'myString'"), or the input object as-is if not a String
	 */
	public static Object quoteIfString(Object obj) {
		return obj instanceof String ? quote((String) obj) : obj;
	}

	/**
	 * Unqualify a string qualified by a '.' dot character. For example, "this.name.is.qualified", returns "qualified".
	 * 
	 * @param qualifiedName
	 *            the qualified name
	 */
	public static String unqualify(String qualifiedName) {
		return unqualify(qualifiedName, '.');
	}

	/**
	 * Unqualify a string qualified by a separator character. For example, "this:name:is:qualified" returns "qualified"
	 * if using a ':' separator.
	 * 
	 * @param qualifiedName
	 *            the qualified name
	 * @param separator
	 *            the separator
	 */
	public static String unqualify(String qualifiedName, char separator) {
		return qualifiedName.substring(qualifiedName.lastIndexOf(separator) + 1);
	}

	/**
	 * Capitalize a <code>String</code>, changing the first letter to upper case as per
	 * {@link Character#toUpperCase(char)}. No other letters are changed.
	 * 
	 * @param str
	 *            the String to capitalize, may be <code>null</code>
	 * @return the capitalized String, <code>null</code> if null
	 */
	public static String capitalize(String str) {
		return changeFirstCharacterCase(str, true);
	}

	/**
	 * Uncapitalize a <code>String</code>, changing the first letter to lower case as per
	 * {@link Character#toLowerCase(char)}. No other letters are changed.
	 * 
	 * @param str
	 *            the String to uncapitalize, may be <code>null</code>
	 * @return the uncapitalized String, <code>null</code> if null
	 */
	public static String uncapitalize(String str) {
		return changeFirstCharacterCase(str, false);
	}

	private static String changeFirstCharacterCase(String str, boolean capitalize) {
		if (str == null || str.length() == 0) {
			return str;
		}
		StringBuffer buf = new StringBuffer(str.length());
		if (capitalize) {
			buf.append(Character.toUpperCase(str.charAt(0)));
		} else {
			buf.append(Character.toLowerCase(str.charAt(0)));
		}
		buf.append(str.substring(1));
		return buf.toString();
	}

	/**
	 * Extract the filename from the given path, e.g. "mypath/myfile.txt" -> "myfile.txt".
	 * 
	 * @param path
	 *            the file path (may be <code>null</code>)
	 * @return the extracted filename, or <code>null</code> if none
	 */
	public static String getFilename(String path) {
		if (path == null) {
			return null;
		}
		int separatorIndex = path.lastIndexOf(FOLDER_SEPARATOR);
		return separatorIndex != -1 ? path.substring(separatorIndex + 1) : path;
	}

	/**
	 * Extract the filename extension from the given path, e.g. "mypath/myfile.txt" -> "txt".
	 * 
	 * @param path
	 *            the file path (may be <code>null</code>)
	 * @return the extracted filename extension, or <code>null</code> if none
	 */
	public static String getFilenameExtension(String path) {
		if (path == null) {
			return null;
		}
		int sepIndex = path.lastIndexOf(EXTENSION_SEPARATOR);
		return sepIndex != -1 ? path.substring(sepIndex + 1) : null;
	}

	/**
	 * Strip the filename extension from the given path, e.g. "mypath/myfile.txt" -> "mypath/myfile".
	 * 
	 * @param path
	 *            the file path (may be <code>null</code>)
	 * @return the path with stripped filename extension, or <code>null</code> if none
	 */
	public static String stripFilenameExtension(String path) {
		if (path == null) {
			return null;
		}
		int sepIndex = path.lastIndexOf(EXTENSION_SEPARATOR);
		return sepIndex != -1 ? path.substring(0, sepIndex) : path;
	}

	/**
	 * Apply the given relative path to the given path, assuming standard Java folder separation (i.e. "/" separators);
	 * 
	 * @param path
	 *            the path to start from (usually a full file path)
	 * @param relativePath
	 *            the relative path to apply (relative to the full file path above)
	 * @return the full file path that results from applying the relative path
	 */
	public static String applyRelativePath(String path, String relativePath) {
		int separatorIndex = path.lastIndexOf(FOLDER_SEPARATOR);
		if (separatorIndex != -1) {
			String newPath = path.substring(0, separatorIndex);
			if (!relativePath.startsWith(FOLDER_SEPARATOR)) {
				newPath += FOLDER_SEPARATOR;
			}
			return newPath + relativePath;
		} else {
			return relativePath;
		}
	}

	/**
	 * Normalize the path by suppressing sequences like "path/.." and inner simple dots.
	 * <p>
	 * The result is convenient for path comparison. For other uses, notice that Windows separators ("\") are replaced
	 * by simple slashes.
	 * 
	 * @param path
	 *            the original path
	 * @return the normalized path
	 */

	public static String cleanPath(String path) {
		String pathToUse = replace(path, WINDOWS_FOLDER_SEPARATOR, FOLDER_SEPARATOR);

		// Strip prefix from path to analyze, to not treat it as part of the
		// first path element. This is necessary to correctly parse paths like
		// "file:core/../core/io/Resource.class", where the ".." should just
		// strip the first "core" directory while keeping the "file:" prefix.
		int prefixIndex = pathToUse.indexOf(":");
		String prefix = "";
		if (prefixIndex != -1) {
			prefix = pathToUse.substring(0, prefixIndex + 1);
			pathToUse = pathToUse.substring(prefixIndex + 1);
		}

		String[] pathArray = delimitedListToStringArray(pathToUse, FOLDER_SEPARATOR);
		List pathElements = new LinkedList();
		int tops = 0;

		for (int i = pathArray.length - 1; i >= 0; i--) {
			if (CURRENT_PATH.equals(pathArray[i])) {
				// Points to current directory - drop it.
			} else if (TOP_PATH.equals(pathArray[i])) {
				// Registering top path found.
				tops++;
			} else {
				if (tops > 0) {
					// Merging path element with corresponding to top path.
					tops--;
				} else {
					// Normal path element found.
					pathElements.add(0, pathArray[i]);
				}
			}
		}

		// Remaining top paths need to be retained.
		for (int i = 0; i < tops; i++) {
			pathElements.add(0, TOP_PATH);
		}

		return prefix + collectionToDelimitedString(pathElements, FOLDER_SEPARATOR);
	}

	/**
	 * Compare two paths after normalization of them.
	 * 
	 * @param path1
	 *            First path for comparizon
	 * @param path2
	 *            Second path for comparizon
	 * @return whether the two paths are equivalent after normalization
	 */
	public static boolean pathEquals(String path1, String path2) {
		return cleanPath(path1).equals(cleanPath(path2));
	}

	/**
	 * Parse the given locale string into a <code>java.util.Locale</code>. This is the inverse operation of Locale's
	 * <code>toString</code>.
	 * 
	 * @param localeString
	 *            the locale string, following <code>java.util.Locale</code>'s toString format ("en", "en_UK", etc).
	 *            Also accepts spaces as separators, as alternative to underscores.
	 * @return a corresponding Locale instance
	 */
	public static Locale parseLocaleString(String localeString) {
		String[] parts = tokenizeToStringArray(localeString, "_ ", false, false);
		String language = parts.length > 0 ? parts[0] : "";
		String country = parts.length > 1 ? parts[1] : "";
		String variant = parts.length > 2 ? parts[2] : "";
		return language.length() > 0 ? new Locale(language, country, variant) : null;
	}

	// ---------------------------------------------------------------------
	// Convenience methods for working with String arrays
	// ---------------------------------------------------------------------

	/**
	 * Append the given String to the given String array, returning a new array consisting of the input array contents
	 * plus the given String.
	 * 
	 * @param array
	 *            the array to append to (can be <code>null</code>)
	 * @param str
	 *            the String to append
	 * @return the new array (never <code>null</code>)
	 */
	public static String[] addStringToArray(String[] array, String str) {
		if (ObjectUtils.isEmpty(array)) {
			return new String[] { str };
		}
		String[] newArr = new String[array.length + 1];
		System.arraycopy(array, 0, newArr, 0, array.length);
		newArr[array.length] = str;
		return newArr;
	}

	/**
	 * Concatenate the given String arrays into one, with overlapping array elements included twice.
	 * <p>
	 * The order of elements in the original arrays is preserved.
	 * 
	 * @param array1
	 *            the first array (can be <code>null</code>)
	 * @param array2
	 *            the second array (can be <code>null</code>)
	 * @return the new array (<code>null</code> if both given arrays were <code>null</code>)
	 */
	public static String[] concatenateStringArrays(String[] array1, String[] array2) {
		if (ObjectUtils.isEmpty(array1)) {
			return array2;
		}
		if (ObjectUtils.isEmpty(array2)) {
			return array1;
		}
		String[] newArr = new String[array1.length + array2.length];
		System.arraycopy(array1, 0, newArr, 0, array1.length);
		System.arraycopy(array2, 0, newArr, array1.length, array2.length);
		return newArr;
	}

	/**
	 * Merge the given String arrays into one, with overlapping array elements only included once.
	 * <p>
	 * The order of elements in the original arrays is preserved (with the exception of overlapping elements, which are
	 * only included on their first occurence).
	 * 
	 * @param array1
	 *            the first array (can be <code>null</code>)
	 * @param array2
	 *            the second array (can be <code>null</code>)
	 * @return the new array (<code>null</code> if both given arrays were <code>null</code>)
	 */
	public static String[] mergeStringArrays(String[] array1, String[] array2) {
		if (ObjectUtils.isEmpty(array1)) {
			return array2;
		}
		if (ObjectUtils.isEmpty(array2)) {
			return array1;
		}
		List result = new ArrayList();
		result.addAll(Arrays.asList(array1));
		for (String element : array2) {
			String str = element;
			if (!result.contains(str)) {
				result.add(str);
			}
		}
		return toStringArray(result);
	}

	/**
	 * Turn given source String array into sorted array.
	 * 
	 * @param array
	 *            the source array
	 * @return the sorted array (never <code>null</code>)
	 */
	public static String[] sortStringArray(String[] array) {
		if (ObjectUtils.isEmpty(array)) {
			return new String[0];
		}
		Arrays.sort(array);
		return array;
	}

	/**
	 * Copy the given Collection into a String array. The Collection must contain String elements only.
	 * 
	 * @param collection
	 *            the Collection to copy
	 * @return the String array (<code>null</code> if the Collection was <code>null</code> as well)
	 */
	public static String[] toStringArray(Collection collection) {
		if (collection == null) {
			return null;
		}
		return (String[]) collection.toArray(new String[collection.size()]);
	}

	/**
	 * Remove duplicate Strings from the given array. Also sorts the array, as it uses a TreeSet.
	 * 
	 * @param array
	 *            the String array
	 * @return an array without duplicates, in natural sort order
	 */
	public static String[] removeDuplicateStrings(String[] array) {
		if (ObjectUtils.isEmpty(array)) {
			return array;
		}
		Set set = new TreeSet();
		for (String element : array) {
			set.add(element);
		}
		return toStringArray(set);
	}

	/**
	 * Split a String at the first occurrence of the delimiter. Does not include the delimiter in the result.
	 * 
	 * @param toSplit
	 *            the string to split
	 * @param delimiter
	 *            to split the string up with
	 * @return a two element array with index 0 being before the delimiter, and index 1 being after the delimiter
	 *         (neither element includes the delimiter); or <code>null</code> if the delimiter wasn't found in the given
	 *         input String
	 */
	public static String[] splitFirst(String toSplit, String delimiter) {
		if (!hasLength(toSplit) || !hasLength(delimiter)) {
			return null;
		}
		int offset = toSplit.indexOf(delimiter);
		if (offset < 0) {
			return null;
		}
		String beforeDelimiter = toSplit.substring(0, offset);
		String afterDelimiter = toSplit.substring(offset + delimiter.length());
		return new String[] { beforeDelimiter, afterDelimiter };
	}

	/**
	 * Take an array Strings and split each element based on the given delimiter. A <code>Properties</code> instance is
	 * then generated, with the left of the delimiter providing the key, and the right of the delimiter providing the
	 * value.
	 * <p>
	 * Will trim both the key and value before adding them to the <code>Properties</code> instance.
	 * 
	 * @param array
	 *            the array to process
	 * @param delimiter
	 *            to split each element using (typically the equals symbol)
	 * @return a <code>Properties</code> instance representing the array contents, or <code>null</code> if the array to
	 *         process was null or empty
	 */
	public static Properties splitArrayElementsIntoProperties(String[] array, String delimiter) {
		return splitArrayElementsIntoProperties(array, delimiter, null);
	}

	/**
	 * Take an array Strings and split each element based on the given delimiter. A <code>Properties</code> instance is
	 * then generated, with the left of the delimiter providing the key, and the right of the delimiter providing the
	 * value.
	 * <p>
	 * Will trim both the key and value before adding them to the <code>Properties</code> instance.
	 * 
	 * @param array
	 *            the array to process
	 * @param delimiter
	 *            to split each element using (typically the equals symbol)
	 * @param charsToDelete
	 *            one or more characters to remove from each element prior to attempting the split operation (typically
	 *            the quotation mark symbol), or <code>null</code> if no removal should occur
	 * @return a <code>Properties</code> instance representing the array contents, or <code>null</code> if the array to
	 *         process was <code>null</code> or empty
	 */
	public static Properties splitArrayElementsIntoProperties(String[] array, String delimiter, String charsToDelete) {

		if (ObjectUtils.isEmpty(array)) {
			return null;
		}
		Properties result = new Properties();
		for (String element2 : array) {
			String element = element2;
			if (charsToDelete != null) {
				element = deleteAny(element2, charsToDelete);
			}
			String[] splittedElement = splitFirst(element, delimiter);
			if (splittedElement == null) {
				continue;
			}
			result.setProperty(splittedElement[0].trim(), splittedElement[1].trim());
		}
		return result;
	}

	/**
	 * Tokenize the given String into a String array via a StringTokenizer. Trims tokens and omits empty tokens.
	 * <p>
	 * The given delimiters string is supposed to consist of any number of delimiter characters. Each of those
	 * characters can be used to separate tokens. A delimiter is always a single character; for multi-character
	 * delimiters, consider using <code>delimitedListToStringArray</code>
	 * 
	 * @param str
	 *            the String to tokenize
	 * @param delimiters
	 *            the delimiter characters, assembled as String (each of those characters is individually considered as
	 *            delimiter).
	 * @return an array of the tokens
	 * @see StringTokenizer
	 * @see String#trim
	 * @see #delimitedListToStringArray
	 */
	public static String[] tokenizeToStringArray(String str, String delimiters) {
		return tokenizeToStringArray(str, delimiters, true, true);
	}

	/**
	 * Tokenize the given String into a String array via a StringTokenizer.
	 * <p>
	 * The given delimiters string is supposed to consist of any number of delimiter characters. Each of those
	 * characters can be used to separate tokens. A delimiter is always a single character; for multi-character
	 * delimiters, consider using <code>delimitedListToStringArray</code>
	 * 
	 * @param str
	 *            the String to tokenize
	 * @param delimiters
	 *            the delimiter characters, assembled as String (each of those characters is individually considered as
	 *            delimiter)
	 * @param trimTokens
	 *            trim the tokens via String's <code>trim</code>
	 * @param ignoreEmptyTokens
	 *            omit empty tokens from the result array (only applies to tokens that are empty after trimming;
	 *            StringTokenizer will not consider subsequent delimiters as token in the first place).
	 * @return an array of the tokens
	 * @see StringTokenizer
	 * @see String#trim
	 * @see #delimitedListToStringArray
	 */
	public static String[] tokenizeToStringArray(String str, String delimiters, boolean trimTokens,
			boolean ignoreEmptyTokens) {

		StringTokenizer st = new StringTokenizer(str, delimiters);
		List tokens = new ArrayList();
		while (st.hasMoreTokens()) {
			String token = st.nextToken();
			if (trimTokens) {
				token = token.trim();
			}
			if (!ignoreEmptyTokens || token.length() > 0) {
				tokens.add(token);
			}
		}
		return toStringArray(tokens);
	}

	/**
	 * Take a String which is a delimited list and convert it to a String array.
	 * <p>
	 * A single delimiter can consists of more than one character: It will still be considered as single delimiter
	 * string, rather than as bunch of potential delimiter characters - in contrast to
	 * <code>tokenizeToStringArray</code>.
	 * 
	 * @param str
	 *            the input String
	 * @param delimiter
	 *            the delimiter between elements (this is a single delimiter, rather than a bunch individual delimiter
	 *            characters)
	 * @return an array of the tokens in the list
	 * @see #tokenizeToStringArray
	 */
	public static String[] delimitedListToStringArray(String str, String delimiter) {
		if (str == null) {
			return new String[0];
		}
		if (delimiter == null) {
			return new String[] { str };
		}
		List result = new ArrayList();
		if ("".equals(delimiter)) {
			for (int i = 0; i < str.length(); i++) {
				result.add(str.substring(i, i + 1));
			}
		} else {
			int pos = 0;
			int delPos = 0;
			while ((delPos = str.indexOf(delimiter, pos)) != -1) {
				result.add(str.substring(pos, delPos));
				pos = delPos + delimiter.length();
			}
			if (str.length() > 0 && pos <= str.length()) {
				// Add rest of String, but not in case of empty input.
				result.add(str.substring(pos));
			}
		}
		return toStringArray(result);
	}

	/**
	 * Convert a CSV list into an array of Strings.
	 * 
	 * @param str
	 *            CSV list
	 * @return an array of Strings, or the empty array if s is null
	 */
	public static String[] commaDelimitedListToStringArray(String str) {
		return delimitedListToStringArray(str, ",");
	}

	/**
	 * Convenience method to convert a CSV string list to a set. Note that this will suppress duplicates.
	 * 
	 * @param str
	 *            CSV String
	 * @return a Set of String entries in the list
	 */
	public static Set commaDelimitedListToSet(String str) {
		Set set = new TreeSet();
		String[] tokens = commaDelimitedListToStringArray(str);
		for (String token : tokens) {
			set.add(token);
		}
		return set;
	}

	/**
	 * Convenience method to return a Collection as a delimited (e.g. CSV) String. E.g. useful for
	 * <code>toString()</code> implementations.
	 * 
	 * @param coll
	 *            Collection to display
	 * @param delim
	 *            delimiter to use (probably a ",")
	 * @param prefix
	 *            string to start each element with
	 * @param suffix
	 *            string to end each element with
	 */
	public static String collectionToDelimitedString(Collection coll, String delim, String prefix, String suffix) {
		if (CollectionUtils.isEmpty(coll)) {
			return "";
		}
		StringBuffer sb = new StringBuffer();
		Iterator it = coll.iterator();
		while (it.hasNext()) {
			sb.append(prefix).append(it.next()).append(suffix);
			if (it.hasNext()) {
				sb.append(delim);
			}
		}
		return sb.toString();
	}

	/**
	 * Convenience method to return a Collection as a delimited (e.g. CSV) String. E.g. useful for
	 * <code>toString()</code> implementations.
	 * 
	 * @param coll
	 *            Collection to display
	 * @param delim
	 *            delimiter to use (probably a ",")
	 */
	public static String collectionToDelimitedString(Collection coll, String delim) {
		return collectionToDelimitedString(coll, delim, "", "");
	}

	/**
	 * Convenience method to return a Collection as a CSV String. E.g. useful for <code>toString()</code>
	 * implementations.
	 * 
	 * @param coll
	 *            Collection to display
	 */
	public static String collectionToCommaDelimitedString(Collection coll) {
		return collectionToDelimitedString(coll, ",");
	}

	/**
	 * Convenience method to return a String array as a delimited (e.g. CSV) String. E.g. useful for
	 * <code>toString()</code> implementations.
	 * 
	 * @param arr
	 *            array to display. Elements may be of any type (toString will be called on each element).
	 * @param delim
	 *            delimiter to use (probably a ",")
	 */
	public static String arrayToDelimitedString(Object[] arr, String delim) {
		if (ObjectUtils.isEmpty(arr)) {
			return "";
		}
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < arr.length; i++) {
			if (i > 0) {
				sb.append(delim);
			}
			sb.append(arr[i]);
		}
		return sb.toString();
	}

	/**
	 * Convenience method to return a String array as a CSV String. E.g. useful for <code>toString()</code>
	 * implementations.
	 * 
	 * @param arr
	 *            array to display. Elements may be of any type (toString will be called on each element).
	 */
	public static String arrayToCommaDelimitedString(Object[] arr) {
		return arrayToDelimitedString(arr, ",");
	}

	// -------------------------------------------------------------------------------------------

	/**
	 * 判断str字符串是否符合我们定义的标准格式，pattern表示格式(正则表达式)。
	 * 
	 * @param str
	 * @param pattern
	 */
	public static boolean isDefinedPattern(String str, String pattern) {
		Assert.notNull(str);
		Assert.notNull(pattern);

		Pattern p = Pattern.compile(pattern);
		Matcher m = p.matcher(str);
		return m.matches();
	}

	/**
	 * 使用str2代替str1中的pattern字符串（pattern可以是正则表达式）
	 * 
	 * @param str1
	 *            将被替换的源字符串
	 * @param pattern
	 *            指定的将被替换的字符串符合的模式（pattern）
	 * @param str2
	 *            用来替换掉符合模式字符串的字符串
	 */
	public static String replaceByPattern(String str1, String pattern, String str2) {
		Assert.notNull(str1);
		Assert.notNull(pattern);
		Assert.notNull(str2);

		Pattern p = Pattern.compile(pattern);
		Matcher m = p.matcher(str1);
		return m.replaceAll(str2);
	}

	/**
	 * 通过指定字符串模式来截取字符串，并且返回切断后的字符串数组
	 * 
	 * <p>
	 * 该方法同StringUtils.split方法有一些区别，StringUtils.split是在分割字符第一次出现的地方把字符串分割成两段， 形成一个length为2的数组。而本方法是在字符串中任何出现符合切割模式
	 * 的地方进行切割，形成一个长度不定的字符串数组
	 * 
	 * @param str
	 * @param pattern
	 */
	public static String[] splitByPattern(String str, String pattern) {
		Assert.notNull(pattern);

		if (IrisStringUtils.hasLength(str)) {
			Pattern p = Pattern.compile(pattern);
			return p.split(str);
		} else {
			return null;
		}
	}

	/**
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isNullOrBlank(String str) {
		if (str == null) {
			return true;
		} else if (str.trim().equals("")) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 去掉字符串中 回车符 换行符 制表符
	 * 
	 * @param str
	 * @return
	 */
	public static String parseToString(String str) {
		if (str == null) {
			return null;
		}
		return str.replaceAll("\r|\n|\t", " ");
	}

	public static String getText(String info, Object... arg) {

		if (arg != null && arg.length > 0) {
			for (int i = 0; i < arg.length; i++) {
				String val = arg[i] == null ? "" : arg[i].toString();
				// String replaceAll(regex, replacement)函数 ,
				// 由于第一个参数支持正则表达式，replacement中出现"$",会按照$1$2的分组
				// 模式进行匹配，当编译器发现"$"后跟的不是整数的时候，就会抛出"非法的组引用"的异常。
				// 所以我们在使用replaceAll(regex,replacement)函数的时候,
				// 要把String中的字符替换成"$AAA"的话，可以对replacement进行"$"的转义处理,filterDollarStr就是用来做转义处理的函数
				info = info.replaceAll("(\\{" + i + "\\})", filterDollarStr(val));
			}
		}
		return info;
	}

	/**
	 * String replaceAll(regex, replacement)函数 , 由于第一个参数支持正则表达式，replacement中出现"$",会按照$1$2的分组
	 * 模式进行匹配，当编译器发现"$"后跟的不是整数的时候，就会抛出"非法的组引用"的异常。 所以我们在使用replaceAll(regex,replacement)函数的时候,
	 * 要把String中的字符替换成"$AAA"的话，可以对replacement进行 "$"的转义处理,filterDollarStr就是用来做转义处理的函数
	 * 
	 * @param str
	 * @return
	 */
	public static String filterDollarStr(String str) {
		StringBuffer sReturn = new StringBuffer("");
		if (str.indexOf('$', 0) > -1) {
			while (str.length() > 0) {
				if (str.indexOf('$', 0) > -1) {
					sReturn.append(str.subSequence(0, str.indexOf('$', 0)));
					sReturn.append("\\$");
					str = str.substring(str.indexOf('$', 0) + 1, str.length());
				} else {
					sReturn.append(str);
					str = "";
				}
			}
		} else {
			sReturn = new StringBuffer(str);
		}
		return sReturn.toString();
	}

	/**
	 * 分割字符串成Integer数组
	 * 
	 * @param strs
	 * @return
	 * @throws ServiceException
	 */
	public static List<Integer> getSplitInt(String strs) throws ServiceException {
		List<Integer> list = new ArrayList<Integer>();
		if (strs == null) {
			return list;
		}
		String[] strGroup = strs.split(",");
		for (String str : strGroup) {
			try {
				Integer value = new Integer(str);
				list.add(value);
			} catch (Exception e) {
				throw new ServiceException("类型转换失败，字符串转换成Integer");
			}
		}

		return list;

	}

	/**
	 * 分割字符串成Long数组
	 * 
	 * @param strs
	 * @return
	 * @throws ServiceException
	 */
	public static List<Long> getSplitLong(String strs) throws ServiceException {
		List<Long> list = new ArrayList<Long>();
		if (strs == null) {
			return list;
		}
		String[] strGroup = strs.split(",");
		for (String str : strGroup) {
			try {
				Long value = new Long(str);
				list.add(value);
			} catch (Exception e) {
				throw new ServiceException("类型转换失败，字符串转换成Long");
			}
		}

		return list;

	}

	/**
	 * 分割字符串,查询的时候用ID的时候超过1000个会出问题的解决方法
	 * 
	 * @param strs
	 * @return
	 */
	public static List<String> getSplitStr(String strs) {
		List<String> list = new ArrayList<String>();
		StringBuffer strTemp = new StringBuffer();
		if (strs == null) {
			return list;
		}
		String[] strGroup = strs.split(",");
		for (int i = 0; i < strGroup.length; i++) {
			if (i != 0 && i % 1000 == 0) {
				list.add(strTemp.toString().substring(0, strTemp.toString().length() - 1));
				strTemp = new StringBuffer();
				strTemp.append(strGroup[i]).append(",");
			} else {
				strTemp.append(strGroup[i]).append(",");
			}
		}
		list.add(strTemp.toString().substring(0, strTemp.toString().length() - 1));
		return list;
	}

	/**
	 * 用户String提供的split方法会过滤掉后面的空字符串，所以提供本方法，严格按照分隔符个数形成数组
	 * 
	 * 例如有一个字符串"1,2,3,4,,,"，使用原生string的split形成的字符串数组为{"1","2","3","4"}， 而用本函数则返回数组{"1","2","3","4","",""}
	 * 
	 * @param input
	 *            本分割的字符串或者StringBuffer对象
	 * @param delimiterPattern
	 *            分割字符
	 * @param limit
	 *            参数控制模式应用的次数，因此影响结果数组的长度。如果该限制 n 大于 0，则模式将被最多应用 n - 1 次，数组的长度将不会大于 n，而且数组的最后项将包含超出最后匹配的定界符的所有输入。如果 n
	 *            为非正，则模式将被应用尽可能多的次数，而且数组可以是任意长度。如果 n 为零，则模式将被应用尽可能多的次数，数组可有任何长度，并且结尾空字符串将被丢弃。
	 * @return
	 */
	public static String[] split(CharSequence input, String delimiterPattern, int limit) {
		int index = 0;
		boolean matchLimited = limit > 0;
		ArrayList matchList = new ArrayList();
		Pattern p = Pattern.compile(delimiterPattern);
		Matcher m = p.matcher(input);

		// Add segments before each match found
		while (m.find()) {
			if (!matchLimited || matchList.size() < limit - 1) {
				String match = input.subSequence(index, m.start()).toString();
				matchList.add(match);
				index = m.end();
			} else if (matchList.size() == limit - 1) { // last one
				String match = input.subSequence(index, input.length()).toString();
				matchList.add(match);
				index = m.end();
			}
		}

		// If no match was found, return this
		if (index == 0) {
			return new String[] { input.toString() };
		}

		// Add remaining segment
		if (!matchLimited || matchList.size() < limit) {
			matchList.add(input.subSequence(index, input.length()).toString());
		}

		// Construct result
		int resultSize = matchList.size();
		// 去掉的代码是同jdk原生split方法的不同之处，例如有一个字符串"1,2,3,4,,,"，使用原生string的split形成的字符串数组为{"1","2","3","4"}，而用本函数则返回数组{"1","2","3","4","",""}
		// if (limit == 0)
		// while (resultSize > 0 && matchList.get(resultSize - 1).equals(""))
		// resultSize--;
		String[] result = new String[resultSize];
		return (String[]) matchList.subList(0, resultSize).toArray(result);
	}

	/**
	 * 判断一个字符串中是否包含一个子串,不区分大小写
	 * 
	 * @param str
	 *            String
	 * @param subString
	 *            String
	 * @return boolean
	 */
	public static boolean isIncludeSubString(String str, String subString) {
		boolean result = false;
		if (str == null || subString == null) {
			return false;
		}
		int strLength = str.length();
		int subStrLength = subString.length();
		String tmpStr = null;
		for (int i = 0; i < strLength; i++) {
			if (strLength - i < subStrLength) {
				return false;
			}
			tmpStr = str.substring(i, subStrLength + i);
			if (tmpStr.endsWith(subString)) {
				return true;
			}
		}
		return result;
	}

	/**
	 * /** 用户String提供的split方法会过滤掉后面的空字符串，所以提供本方法，严格按照分隔符个数形成数组
	 * 
	 * 例如有一个字符串"1,2,3,4,,,"，使用原生string的split形成的字符串数组为{"1","2","3","4"}， 而用本函数则返回数组{"1","2","3","4","",""}
	 * 
	 * @param input
	 *            本分割的字符串或者StringBuffer对象
	 * @param delimiterPattern
	 *            分割字符
	 * @return
	 */
	public static String[] split(CharSequence input, String delimiterPattern) {
		return split(input, delimiterPattern, 0);
	}

	/**
	 * 将字符串str 按照length长度为一个字符串平均分割。 如果length<=0，则返回null。 wuwm
	 * 
	 * @param str
	 * @param length
	 * @return String[]
	 */
	public static String[] splitSameLegthArray(String str, int length) {
		if (length <= 0 || str == null) {
			return null;
		}
		String[] strArr = new String[str.length() / length + 1];

		char[] cStr = str.toCharArray();
		StringBuffer buf = new StringBuffer();
		int count = 0;
		int leg = 0;
		for (char s : cStr) {
			if (count != length) {
				buf.append(String.valueOf(s));
				count++;
			} else {
				strArr[leg++] = buf.toString();
				buf.delete(0, buf.length());
				buf.append(String.valueOf(s));
				count = 1;
			}
		}
		if (buf.length() != 0) {
			strArr[leg++] = buf.toString();
		}
		return strArr;
	}

	public static Properties toProperties(String str) {
		return toProperties(str, ",");
	}

	public static Properties toProperties(String str, String split) {
		Properties props = new Properties();
		if (!IrisStringUtils.hasLength(str)) {
			return props;
		}
		String[] lines = str.split(split);
		for (String line : lines) {
			if (line == null) {
				return null;
			}
			line = IrisStringUtils.trimLeadingWhitespace(line);
			if (line.length() > 0) {
				char firstChar = line.charAt(0);
				if (firstChar != '#' && firstChar != '!') {
					int separatorIndex = line.indexOf("=");
					if (separatorIndex == -1) {
						separatorIndex = line.indexOf(":");
					}
					String key = separatorIndex != -1 ? line.substring(0, separatorIndex) : line;
					String value = separatorIndex != -1 ? line.substring(separatorIndex + 1) : "";
					key = IrisStringUtils.trimTrailingWhitespace(key);
					value = IrisStringUtils.trimLeadingWhitespace(value);
					props.put(key, value);
				}
			}
		}
		return props;
	}

	public static String toString(Object obj) {
		return obj == null ? "" : obj.toString();
	}

	// 金额名称样式
	static String[] partern = { "元", "拾", "百", "千", "万", "拾", "百", "千", "亿", "拾", "百", "千", "兆" };

	/**
	 * Constructor for StringUtils.
	 */
	public IrisStringUtils() {
		super();
	}

	/**
	 * 替换字符串函数 String strSource - 源字符串 String strFrom - 要替换的子串 String strTo - 替换为的字符串
	 */
	public static String replaceStrs(String strSource, String strFrom, String strTo) {
		// 如果要替换的子串为空，则直接返回源串
		if (strFrom == null || strFrom.equals("")) {
			return strSource;
		}
		String strDest = "";
		// 要替换的子串长度
		int intFromLen = strFrom.length();
		int intPos;
		// 循环替换字符串
		while ((intPos = strSource.indexOf(strFrom)) != -1) {
			// 获取匹配字符串的左边子串
			strDest = strDest + strSource.substring(0, intPos);
			// 加上替换后的子串
			strDest = strDest + strTo;
			// 修改源串为匹配子串后的子串
			strSource = strSource.substring(intPos + intFromLen);
		}
		// 加上没有匹配的子串
		strDest = strDest + strSource;
		// 返回
		return strDest;
	}

	public static String filterNull(Object obj) {
		if (obj == null) {
			return "";
		}
		// if(String.valueOf(obj)=="null") return "";
		return String.valueOf(obj);
	}

	public static String replaceRN(String strSource) {
		char c1 = 10;
		char c2 = 13;
		String result = IrisStringUtils.replace(strSource, String.valueOf(c2) + String.valueOf(c1), "&#13;&#10;");
		return result;
	}

	/**
	 * <p>
	 * The method search for the specified pattern and replaces the all occurances of the given pattern with the
	 * specifed arg String.
	 * </p>
	 * 
	 * @param source
	 *            is the source string that we would like to apply the replacement on
	 * @param pattern
	 *            is the pattern in that string that we are looking to replace
	 * @param arg
	 *            is the string that we will replace the pattern segment with
	 * @return The substituted <code>String</code>.
	 */
	/*
	 * public static String replace( String source , String pattern , String arg ) { if ( source == null ) return null;
	 * String result = source; int startIndex = source.indexOf( pattern ); while ( startIndex != -1 ) { result =
	 * result.substring( 0 , startIndex ) + arg + result.substring( Math.min( startIndex + pattern.length() ,
	 * result.length() ) ); int offset = startIndex + arg.length(); if ( offset > result.length() ) return result;
	 * startIndex = result.indexOf( pattern , offset ); } return result; }
	 */
	/**
	 * Returns a string consisting of "s", plus enough copies of "pad_ch" on the left hand side to make the length of
	 * "s" equal to or greater than len (if "s" is already longer than "len", then "s" is returned).
	 */

	/**
	 * 如果字符串不符合规定长度，在字符串左边补充指定字符
	 * 
	 * @param s原字符串
	 * @param len字符串应有长度
	 * @param pad_ch指定补充字符
	 * @return
	 */
	public static String padLeft(String s, int len, char pad_ch) {
		if (s.length() >= len) {
			return s;
		} else {
			StringBuilder sb = new StringBuilder();
			int n = len - s.length();
			for (int i = 0; i < n; i++) {
				sb.append(pad_ch);
			}
			sb.append(s);
			return sb.toString();
		}
	}

	/**
	 * 如果字符串不符合规定长度，在字符串右边补充指定字符
	 * 
	 * @param s原字符串
	 * @param len字符串应有长度
	 * @param pad_ch指定补充字符
	 * @return
	 */
	public static String padRight(String s, int len, char pad_ch) {
		if (s.length() >= len) {
			return s;
		} else {
			StringBuilder sb = new StringBuilder();
			int n = len - s.length();
			sb.append(s);
			for (int i = 0; i < n; i++) {
				sb.append(pad_ch);
			}
			return sb.toString();
		}
	}

	/**
	 * Returns a string consisting of "s", with each of the first "len" characters replaced by "mask_ch" character.
	 */
	public static String maskLeft(String s, int len, char mask_ch) {
		if (len <= 0) {
			return s;
		}
		len = Math.min(len, s.length());
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < len; i++) {
			sb.append(mask_ch);
		}
		sb.append(s.substring(len));
		return sb.toString();
	}

	/**
	 * Returns a string consisting of "s", with each of the last "len" characters replaces by "mask_ch" character.
	 */
	public static String maskRight(String s, int len, char mask_ch) {
		if (len <= 0) {
			return s;
		}
		len = Math.min(len, s.length());
		StringBuilder sb = new StringBuilder();
		sb.append(s.substring(0, s.length() - len));
		for (int i = 0; i < len; i++) {
			sb.append(mask_ch);
		}
		return sb.toString();
	}

	public static String switchMoneyChinese(BigDecimal bg) throws Exception {
		// 校验入口参数
		if (bg == null) {
			throw new Exception("输入金额不正确！");
		}

		// 转换为字符形态
		String amt = bg.toString();

		if ("0".equalsIgnoreCase(amt) || "0.0".equalsIgnoreCase(amt) || "0.00".equalsIgnoreCase(amt)) {
			return "零元整";
		}

		// 校验小数位数是否小于两位
		int local = amt.indexOf(".");
		if (local != -1 && amt.substring(local + 1).length() > 2) {
			throw new Exception("输入金额不能操过两位小数！");
		}

		// 将金额字串转为整数和小数部分
		String temp[] = switchIntAndDig(amt);

		StringBuffer sb = new StringBuffer();
		sb.append(generateIntegerChinese(temp[0]));
		sb.append(generateDigitalChinese(temp[1]));

		return sb.toString();
	}

	// 产生整数位的金额大写
	private static String generateIntegerChinese(String digital) throws Exception {
		// 参数校验
		if (digital == null || digital.length() > 13) {
			throw new Exception("输入的整数位数不正确或者金额过于庞大！");
		}

		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < digital.length(); i++) {
			char ch = digital.charAt(i);
			if (digital.length() - i == 5 && ch == '0') {
				sb.append("万");
			}

			if (digital.length() - i == 9 && ch == '0') {
				sb.append("亿");
			}

			if (i == digital.length() - 1 && ch == '0') {
				sb.append("元");
			}

			if (ch == '0') {
				continue;
			}
			sb.append(swtichtoChinese(ch));
			sb.append(partern[digital.length() - 1 - i]);
		}
		return sb.toString();
	}

	// 产生小数位的金额大写
	private static String generateDigitalChinese(String digital) throws Exception {
		// 参数校验
		if (digital == null || digital.length() > 2) {
			throw new Exception("输入的小数位数不正确，请在两位之内！");
		}

		// 小数为0的判断处理
		if ("00".equals(digital) || "0".equals(digital)) {
			return "整";
		}

		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < digital.length(); i++) {
			char ch = digital.charAt(i);
			if (ch == '0') {
				continue;
			}
			sb.append(swtichtoChinese(ch));
			if (i == 0) {
				sb.append("角");
			} else {
				sb.append("分");
			}

		}
		return sb.toString();
	}

	// 将金额字串转为整数和小数部分
	private static String[] switchIntAndDig(String money) throws Exception {
		// 定义会传数组
		String[] rslt = { "", "" };

		// 检查入口参数
		try {
			new BigDecimal(money);
		} catch (Exception e) {
			throw new Exception("输入参数不正确！");
		}

		// 分解数组，如果没有小数位，小数位用0表示。
		int local = money.indexOf(".");
		if (local == -1) {
			rslt[0] = money;
			rslt[1] = "0";
		} else {
			rslt[0] = money.substring(0, local);
			rslt[1] = money.substring(local + 1);
		}

		return rslt;
	}

	// 将阿拉伯数字转为中文数字
	private static String swtichtoChinese(char ch) throws Exception {
		switch (ch) {
		case '0':
			return "零";
		case '1':
			return "壹";
		case '2':
			return "贰";
		case '3':
			return "叁";
		case '4':
			return "肆";
		case '5':
			return "伍";
		case '6':
			return "陆";
		case '7':
			return "柒";
		case '8':
			return "捌";
		case '9':
			return "玖";
		default:
			throw new Exception("输入字符不正确");
		}
	}

	/**
	 * add by thw 返回单个金额大写值
	 * 
	 * @param request
	 * @param bg
	 * @param flag
	 *            如果同一request中有多个金额值显示，可以加flag来区分
	 * @throws Exception
	 */
	public static void genChineseMoneyTable(HttpServletRequest request, BigDecimal bg, String flag) throws Exception {
		StringBuilder sb = new StringBuilder("");
		// 将金额字串转为整数和小数部分
		String amt = bg.toString();
		String temp[] = switchIntAndDig(amt);
		String arr = padLeft(temp[0], 8, '0');
		String str = "";
		for (int i = 0; i < arr.length(); i++) {
			str = arr.substring(i, i + 1);
			if (i == 0) {
				if ("0".equals(str)) {
					request.setAttribute(flag + "QW", "&nbsp;零");
				} else {
					request.setAttribute(flag + "QW", "&nbsp;" + swtichtoChinese(str.charAt(0)));
				}
			} else if (i == 1) {
				if ("0".equals(str)) {
					request.setAttribute(flag + "BW", "&nbsp;零");
				} else {
					request.setAttribute(flag + "BW", "&nbsp;" + swtichtoChinese(str.charAt(0)));
				}
			} else if (i == 2) {
				if ("0".equals(str)) {
					request.setAttribute(flag + "SW", "&nbsp;零");
				} else {
					request.setAttribute(flag + "SW", "&nbsp;" + swtichtoChinese(str.charAt(0)));
				}
			} else if (i == 3) {
				if ("0".equals(str)) {
					request.setAttribute(flag + "W", "&nbsp;零");
				} else {
					request.setAttribute(flag + "W", "&nbsp;" + swtichtoChinese(str.charAt(0)));
				}
			} else if (i == 4) {
				if ("0".equals(str)) {
					request.setAttribute(flag + "Q", "零&nbsp;");
				} else {
					request.setAttribute(flag + "Q", "&nbsp;" + swtichtoChinese(str.charAt(0)));
				}
			} else if (i == 5) {
				if ("0".equals(str)) {
					request.setAttribute(flag + "B", "零&nbsp;");
				} else {
					request.setAttribute(flag + "B", "&nbsp;" + swtichtoChinese(str.charAt(0)));
				}
			} else if (i == 6) {
				if ("0".equals(str)) {
					request.setAttribute(flag + "S", "&nbsp;零");
				} else {
					request.setAttribute(flag + "S", "&nbsp;" + swtichtoChinese(str.charAt(0)));
				}
			} else if (i == 7) {
				if ("0".equals(str)) {
					request.setAttribute(flag + "Y", "&nbsp;零");
				} else {
					request.setAttribute(flag + "Y", "&nbsp;" + swtichtoChinese(str.charAt(0)));
				}
			}
		}
	}

	/**
	 * add by lwy 20081222
	 * 
	 * @param strIn
	 * @param regexString
	 * @param target
	 * @return
	 */
	public static String regexReplaceString(String strIn, String regexString, Object target) {
		String targetString = filterNull(target);
		return regexReplaceString(strIn, regexString, targetString);
	}

	public static String regexReplaceString(String strIn, String regexString, String target) {

		Pattern p = Pattern.compile(regexString, Pattern.CASE_INSENSITIVE);
		String s = strIn;
		Matcher m = p.matcher(s);

		StringBuffer sb = new StringBuffer();
		int i = 0;
		boolean result = m.find();
		while (result) {
			i++;
			try {
				m.appendReplacement(sb, target.replace("$", "\\$"));
			} catch (Exception e) {
				e.printStackTrace();
			}
			result = m.find();
		}
		m.appendTail(sb);

		return sb.toString();
	}

	public static String StripHTML(String input) {
		StringBuffer output = new StringBuffer();
		StringBuffer temp = new StringBuffer();
		boolean inHTML = false;
		for (int i = 0; i < input.length(); i++) {
			char c = input.charAt(i);
			if (c == '<') {

				if (inHTML) {
					output.append(temp.toString());
					temp = new StringBuffer();
				}
				inHTML = true;
				temp.append(c);
			} else {
				if (c == '>') {
					inHTML = false;
					if (temp.toString().length() > 1) {
						temp = new StringBuffer();
					} else {
						output.append(temp.toString());
						output.append(c);
						temp = new StringBuffer();
					}
				} else {
					if (!inHTML) {
						output.append(c);

					} else {
						temp.append(c);
					}
				}
			}
		}
		output.append(temp.toString());
		return output.toString();
	}

	public static String StripHTML1(String source) {

		String result;

		// Remove HTML Development formatting
		result = source.replace("\r", " "); // Replace line breaks with space
											// because browsers inserts space
		result = result.replace("\n", " "); // Replace line breaks with space
											// because browsers inserts space
		result = result.replace("\t", ""); // Remove step-formatting
		result = regexReplaceString(result, "( )+", " "); // Remove repeating
															// speces becuase
															// browsers ignore
															// them

		// Remove the header (prepare first by clearing attributes)
		result = regexReplaceString(result, "<( )*head([^>])*>", "<head>");
		result = regexReplaceString(result, "(<( )*(/)( )*head( )*>)", "</head>");
		result = regexReplaceString(result, "(<head>).*(</head>)", "");

		// remove all scripts (prepare first by clearing attributes)
		result = regexReplaceString(result, "<( )*script([^>])*>", "<script>");
		result = regexReplaceString(result, "(<( )*(/)( )*script( )*>)", "</script>");
		// result = regexReplaceString(result,
		// "(<script>)([^(<script>\.</script>)])*(</script>)","");
		result = regexReplaceString(result, "(<script>).*(</script>)", "");

		// remove all styles (prepare first by clearing attributes)
		result = regexReplaceString(result, "<( )*style([^>])*>", "<style>");
		result = regexReplaceString(result, "(<( )*(/)( )*style( )*>)", "</style>");
		result = regexReplaceString(result, "(<style>).*(</style>)", "");

		// insert tabs in spaces of <td> tags
		result = regexReplaceString(result, "<( )*td([^>])*>", "\t");

		// insert line breaks in places of <BR> and <LI> tags
		result = regexReplaceString(result, "<( )*br( )*>", "\r");
		result = regexReplaceString(result, "<( )*li( )*>", "\r");

		// insert line paragraphs (double line breaks) in place if <P>, <DIV>
		// and <TR> tags
		result = regexReplaceString(result, "<( )*div([^>])*>", "\r\r");
		result = regexReplaceString(result, "<( )*tr([^>])*>", "\r\r");
		result = regexReplaceString(result, "<( )*p([^>])*>", "\r\r");

		// Remove remaining tags like <a>, links, images, comments etc -
		// anything thats enclosed inside < >
		result = regexReplaceString(result, "<[^>]*>", "");

		// replace special characters:
		result = regexReplaceString(result, "&nbsp;", " ");

		result = regexReplaceString(result, "&bull;", " * ");
		result = regexReplaceString(result, "&lsaquo;", "<");
		result = regexReplaceString(result, "&rsaquo;", ">");
		result = regexReplaceString(result, "&trade;", "(tm)");
		result = regexReplaceString(result, "&frasl;", "/");
		// result = regexReplaceString(result, "<","<");
		// result = regexReplaceString(result, ">",">");
		result = regexReplaceString(result, "&copy;", "(c)");
		result = regexReplaceString(result, "&reg;", "(r)");
		// Remove all others. More can be added, see
		// http://hotwired.lycos.com/webmonkey/reference/special_characters/
		result = regexReplaceString(result, "&(.{2,6});", "");

		// for testng
		// regexReplaceString(result, this.txtRegex.Text,"");

		// make line breaking consistent
		result = result.replace("\n", "\r");

		// Remove extra line breaks and tabs: replace over 2 breaks with 2 and
		// over 4 tabs with 4.
		// Prepare first to remove any whitespaces inbetween the escaped
		// characters and remove redundant tabs inbetween linebreaks
		result = regexReplaceString(result, "(\r)( )+(\r)", "\r\r");
		result = regexReplaceString(result, "(\t)( )+(\t)", "\t\t");
		result = regexReplaceString(result, "(\t)( )+(\r)", "\t\r");
		result = regexReplaceString(result, "(\r)( )+(\t)", "\r\t");
		result = regexReplaceString(result, "(\r)(\t)+(\r)", "\r\r"); // Remove
																		// redundant
																		// tabs
		result = regexReplaceString(result, "(\r)(\t)+", "\r\t"); // Remove
																	// multible
																	// tabs
																	// followind
																	// a
																	// linebreak
																	// with just
																	// one tab
		String breaks = "\r\r\r"; // Initial replacement target string for
									// linebreaks
		String tabs = "\t\t\t\t\t"; // Initial replacement target string for
									// tabs
		for (int index = 0; index < result.length(); index++) {
			result = result.replace(breaks, "\r\r");
			result = result.replace(tabs, "\t\t\t\t");
			breaks = breaks + "\r";
			tabs = tabs + "\t";
		}

		// Thats it.
		return result;

	}

	/**
	 * add by lgq，rtf转换.
	 * 
	 * @param rtf
	 * @return
	 * @throws Exception
	 */
	public static String RtfToString(String rtf) throws Exception {
		// rtf=rtf.replaceAll("\\\\ldblquote","\\\\'A1\\\\'B0");
		// rtf=rtf.replaceAll("\\\\rdblquote","\\\\'A1\\\\'B1");

		RTFEditorKit rtf_edit = new RTFEditorKit();

		DefaultStyledDocument doc = new DefaultStyledDocument();

		StringReader reader = new StringReader(rtf);

		rtf_edit.read(reader, doc, 0);

		String text = doc.getText(0, doc.getLength());
		text = new String(text.getBytes("ISO8859-1"), "GBK");
		text = text.replaceAll(" ", "&nbsp;");
		text = text.replaceAll("\\n", "<br>");
		text = text.replaceAll("\\r", "<br>");
		return text;
	}

	/**
	 * 数据库的内容转化为html格式.
	 * 
	 * @param source
	 * @return
	 */
	public static String replaceToHtml(String source) {
		if (isNullOrBlank(source)) {
			return "";
		}
		return source.replace("\r\n", "<br>").replace("\n", "<br>");
	}

	/**
	 * add by zxg，判断字符串中有多少汉字（包括繁体）.
	 * 
	 * @param str
	 * @return
	 */
	public static int countZH(String str) {
		int count = 0;
		String regEx = "[\u4E00-\u9FA5\uFE30-\uFFA0]";
		Pattern p = Pattern.compile(regEx);
		Matcher m = p.matcher(str);
		while (m.find()) {
			for (int i = 0; i <= m.groupCount(); i++) {
				count = count + 1;
			}
		}
		return count;
		// System.out.println("共有 " + count + "个 ");
	}

	/**
	 * 判断字符串中是否存在"[@"和"@]".
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isExistParam(String str) {
		if (str == null) {
			return false;
		}
		if (str.contains("[@") && str.contains("@]")) {
			return true;
		}
		return false;
	}

	/**
	 * 判断map所有value中是否存在"[@"和"@]".
	 * 
	 * @param map
	 * @return
	 */
	public static boolean isExistParam(Map<String, Object> map) {
		if (map == null) {
			return false;
		}
		Set<String> keySet = map.keySet();
		for (Object element : keySet) {
			String key = (String) element;
			String value = (String) map.get(key);
			if (isExistParam(value)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 通过业务类传过来的参数对SQL中对应的参数进行替换,SQL中参数格式为[@参数名@].
	 * 
	 * @param sql
	 * @param map
	 * @return
	 */
	public static String transSql(String content, Map<?, ?> map, List<Object> params) {
		Pattern p = Pattern.compile("\\[@\\S[^@]*@\\]", 2);// 正则表达式，\\S表示去掉空白字符，如空格、回车等，*表示任意符号，值2是表示大小写不限制
		Matcher m = p.matcher(content);
		String key;
		String paramKey;

		while (m.find()) {
			key = m.group().toLowerCase();
			paramKey = key.substring(2, key.length() - 2);// 去掉[@ @]
			content = IrisStringUtils.regexReplaceString(content, "\\[@" + paramKey + "@\\]", "?");
			params.add(map.get(paramKey));
		}
		return content;

	}
	
	public static String transSql2(String content, Map<?, ?> map, List<Object> params) {
		Pattern p = Pattern.compile("\\[@\\S[^@]*@\\]", 2);// 正则表达式，\\S表示去掉空白字符，如空格、回车等，*表示任意符号，值2是表示大小写不限制
		Matcher m = p.matcher(content);
		String key;
		String paramKey;

		while (m.find()) {
			key = m.group();
			paramKey = key.substring(2, key.length() - 2);// 去掉[@ @]
			content = IrisStringUtils.regexReplaceString(content, "\\[@" + paramKey + "@\\]", "?");
			params.add(map.get(paramKey));
		}
		return content;

	}

	/**
	 * 去除字符串中的空格、回车、换行符、制表符，本方法采用的是java的正则表达式.
	 * 
	 * @param str
	 */
	public static String replaceBlank(String str) {
		Pattern p = Pattern.compile("\\s*|\t|\r|\n");
		Matcher m = p.matcher(str);
		String after = m.replaceAll("");
		return after;

	}

	/**
	 * 字符串转化为list.
	 * 
	 * @param str
	 * @param regex
	 *            分隔符
	 * @return
	 */
	public static List<String> splitStrToList(String str, String regex) {
		List<String> list = new ArrayList<String>();
		if (str == null) {
			return list;
		}
		if (regex == null) {
			regex = ",";
		}
		String[] strAry = str.split(regex);
		for (String element : strAry) {
			list.add(element);
		}
		return list;
	}

	/**
	 * 从指定字符串中萃取 某种规则的一批字符串.
	 * 
	 * @author lineshow created on 2011-11-30
	 * @param srcStr
	 * @param regex
	 *            eg.(default)"\\[@([A-Za-z|_]+)@\\]"
	 * @param case_state
	 *            [1:upper;0:ignore;-1:lower]
	 * @return
	 */
	public static List<String> extractFromSpecStr(String srcStr, String regex, int case_state) {
		if (StringUtils.isEmpty(srcStr)) {
			return new ArrayList<String>();
		}
		if (StringUtils.isEmpty(regex)) {
			regex = "\\[@([A-Za-z|_]+)@\\]";
		}

		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(srcStr);

		List<String> extractList = new ArrayList<String>();
		while (matcher.find()) {
			switch (case_state) {
			case 1:
				extractList.add(matcher.group(1).toUpperCase());
				break;
			case -1:
				extractList.add(matcher.group(1).toLowerCase());
				break;
			default:
				extractList.add(matcher.group(1));
			}
		}

		return extractList;
	}

	/**
	 * 全角转半角
	 * 
	 * @param full
	 * @return String
	 */
	public static String full2Half(String full) {
		char c[] = full.toCharArray();
		for (int i = 0; i < c.length; i++) {
			if (c[i] == '\u3000') {
				c[i] = ' ';
			} else if (c[i] > '\uFF00' && c[i] < '\uFF5F') {
				c[i] = (char) (c[i] - 65248);
			}
		}
		return new String(c);
	}

	// 去除增补字符 unicode说明定义的算法 计算出增补字符范围0x10000 至 0x10FFFF
	public static String filterSupplementaryChars(String text) {

		StringBuilder sb = new StringBuilder();

		if (text == null) {
			return "";
		}
		char[] data = text.toCharArray();
		for (int i = 0, len = data.length; i < len; i++) {

			char c = data[i];
			char high = c;
			char low;
			if (!Character.isHighSurrogate(high)) {
				sb.append(c);
				continue;

			}
			if (i + 1 == len) {
				break;
			}
			low = data[i + 1];
			// 先判断是否在代理范围（surrogate blocks）
			// 增补字符编码为两个代码单元，
			// 第一个单元来自于高代理（high surrogate）范围（0xD800 至 0xDBFF），
			// 第二个单元来自于低代理（low surrogate）范围（0xDC00 至 0xDFFF）。

			if (Character.isSurrogatePair(high, low)) { // 如果在代理范围

				int codePoint = Character.toCodePoint(high, low);
				if (Character.isSupplementaryCodePoint(codePoint)) {

					i++;

				} else {

					sb.append(c);

				}

			} else {

				sb.append(c);
			}

		}

		return sb.toString();
	}

	/**
	 * 
	 * XHTML Encoding. Replaces all '&gt;' '&lt;' '&amp;', "'" and '"' with entity reference suitable for XHTML decoding
	 * 
	 * in common user agents (including legacy user agents, which do not decode "&apos;" to "'", so "&#39;" is used
	 * 
	 * instead [see http://www.w3.org/TR/xhtml1/#C_16])
	 */

	public static String XHTMLEnc(String s) {

		return XMLOrXHTMLEnc(s, "&#39;");

	}

	/**
	 * 
	 * 
	 * 
	 * @param s
	 * 
	 * @param aposReplacement
	 * 
	 * @return
	 */

	public static String XMLOrXHTMLEnc(String s, String aposReplacement) {

		int ln = s.length();

		for (int i = 0; i < ln; i++) {

			char c = s.charAt(i);

			if (c == '<' || c == '>' || c == '&' || c == '"' || c == '\'') {

				StringBuffer b = new StringBuffer(s.substring(0, i));

				switch (c) {

				case '<':

					b.append("&lt;");

					break;

				case '>':

					b.append("&gt;");

					break;

				case '&':

					b.append("&amp;");

					break;

				case '"':

					b.append("&quot;");

					break;

				case '\'':

					b.append(aposReplacement);

					break;

				}

				i++;

				int next = i;

				while (i < ln) {

					c = s.charAt(i);

					if (c == '<' || c == '>' || c == '&' || c == '"' || c == '\'') {

						b.append(s.substring(next, i));

						switch (c) {

						case '<':

							b.append("&lt;");

							break;

						case '>':

							b.append("&gt;");

							break;

						case '&':

							b.append("&amp;");

							break;

						case '"':

							b.append("&quot;");

							break;

						case '\'':

							b.append(aposReplacement);

							break;

						}

						next = i + 1;

					}

					i++;

				}

				if (next < ln) {

					b.append(s.substring(next));

				}

				s = b.toString();

				break;

			} // if c ==

		} // for

		return s;

	}
	public static String getIP(HttpServletRequest request) {
		String ip = request.getHeader("X-Forwarded-For");

		if (ip == null || ip.length() == 0) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}

		if (ip == null || ip.length() == 0) {
			ip = request.getRemoteAddr();
		}

		return ip;
	}

	public static void main(String... strings) {

		String ss = " from org_department t where org_code = [@org_Code@] and dept_name like '%' || [@startwith@] || '%' and dept_code =[@dept_code@] and dept_name like '%' || [@startWith@] || '%'";
		List<String> keylist = extractFromSpecStr(ss, null, 1);

		for (String str : keylist) {
			System.out.println(str);
		}
	}
	
	/**
	 * 半角转全角 from http://www.cnblogs.com/modou/articles/2679815.html
	 * 
	 * @param input
	 *            String.
	 * @return 全角字符串.
	 */
	public static String half2Full(String input) {
		char c[] = input.toCharArray();
		for (int i = 0; i < c.length; i++) {
			if (c[i] == ' ') {
				c[i] = '\u3000';
			} else if (c[i] < '\177') {
				c[i] = (char) (c[i] + 65248);

			}
		}
		return new String(c);
	}
	public static String oracleClob2Str(Clob clob) throws Exception {
		return (clob != null ? clob.getSubString(1, (int) clob.length()) : null);
	}
}