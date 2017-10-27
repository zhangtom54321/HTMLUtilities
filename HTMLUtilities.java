/**
 *	Utilities for handling HTML code. It breaks HTML files into tokens, and stores them in an array.
 *
 *	@author Tom Zhang
 *	@version 10/26/17
 */
 
public class HTMLUtilities {

	// NONE = not nested in a block, COMMENT = inside a comment block
	// PREFORMAT = inside a pre-format block
	private enum TokenState { NONE, COMMENT, PREFORMAT };
	// the current tokenizer state
	private static TokenState state;
	
	private final String COMMENT_START = "<!--";
	private final String COMMENT_END = "-->";
	
	public HTMLUtilities() {
		state = TokenState.NONE;
	}
	
	/**
	 * The main utility in this class.
	 * It takes a string as input, then tokenizes the string and passes back
	 * an array of string tokens
	 * 
	 * @param str		the input string to tokenize
	 * @return			the array of string tokens
	 * 
	 */
	public String[] tokenizeHTMLString(String str) {
		String[] result = new String[999999];	// make the size of the array large!
		int resultIndex = -1;
		
		int strIndex = 0;
		
		// string to hold token
		String token = "";
		boolean isToken = false;
		boolean isTag = false;
		
		while (strIndex < str.length()) {
			/*if (str.indexOf(DOCTYPE_TAG) == strIndex) {
				strIndex+= DOCTYPE_TAG.length()-1;
			}*/
			
			if (str.indexOf(COMMENT_START, strIndex) == strIndex) { 
			// if the first occurence of the comment start tag starting from the current position
			// IS the current position, the following is inside a token.
				state = TokenState.COMMENT;
			}
			if (str.indexOf(COMMENT_END, strIndex) == strIndex) {
			// if the first occurence of the comment end tag starting from the current position
			// IS the current position, the following is inside a token.
				state = TokenState.NONE;
				strIndex+=(COMMENT_END.length());
			}
			if (state == TokenState.NONE && strIndex < str.length()){
				char c = str.charAt(strIndex);
				if (c == '<'){ // To see if the next token is a tag
					isToken = true;
					isTag = true;
					if (token.length() > 0) {
						resultIndex++;
						result[resultIndex] = token;
						token = "";
					}
				}
				if (isTag) { // protocol for if the next token is a tag
					if (c == '>'){ // If it is the end of the tag, the next characters are not part of the current token
						isToken = false;
					}
					token += c;
					if (token.length() > 0 && !isToken) { // storing the current token if it is the end of the tag
						resultIndex++;
						isTag = false;
						result[resultIndex] = token;
						token = "";
					}
				}
				else { // if isTag == false
					if (!isPunctuation(c) && !Character.isWhitespace(c)) {
						isToken = true;
						token+=c;
					}
					else {
						isToken = false;
					}
					if (token.length() > 0 && !isToken) { // if the token has a non-zero length
					// and there is a punctuation mark or whitespace at the current character,
					// store the current token in the array, and reset token.
						resultIndex++;
						result[resultIndex] = token;
						token = "";
					}
					if (isPunctuation(c)) { // if the current character is a punctuation mark
					// store it as its own token in the array
						resultIndex++;
						result[resultIndex] = "" + c;
						token = "";
					}
					if (strIndex+1 < str.length() && 
						(isLetter(c) && isNumber(str.charAt(strIndex+1)) ||
						isNumber(c) && isLetter(str.charAt(strIndex+1)))) {
					// If the next index is within the max length,
					// and the character and the one following it are not both letters
					// or both numbers:
					// store the current token in the array, and reset token.
							resultIndex++;
							result[resultIndex] = token;
							token = "";
					}
				}
			}
			strIndex++;
			
			
		}
		// in case last token goes to end of str
		if (token.length() > 0) {
			resultIndex++;
			result[resultIndex] = token;
		}
		result = sizeArray(result, resultIndex+1);
		
		return result;
	}
	/**
	 * Takes a large String array as input and outputs a copy String
	 * array that is exactly the size of the number of valid elements
	 * @param arr			the input String array
	 * @param size			the number of valid elements in array
	 * @return				a copy of the String array with exactly
	 * 						the number of valid elements
	*/
	public String[] sizeArray(String[] arr, int num) {
		String[] result =  new String[num];
		for (int c = 0; c < num; c++) {
			result[c] = arr[c];
		}
		return result;
	}
	
	/**
	 *	Checks if a character is a punctuation character or not.
	 *	@param  c			Character to be checked
	 *  @return boolean		Returns true if the character is a punctuation character
	 *						Returns false if the character is not a punctuation character
	 */
	private boolean isPunctuation(char c) {
		if ((c >= 'A' && c <= 'Z') || (c >= 'a' && c <= 'z') ||
			(c >= '0' && c <= '9') || Character.isWhitespace(c))
				return false;
		return true;
	}
	
	/**
	 *	Checks if a character is a letter or not.
	 *	@param c			Character to be checked
	 *	@return boolean		Returns true if the character is a letter
	 *						Returns false if the character is not a letter
	 */
	 private boolean isLetter(char c) {
	 	if ((c >= 'A' && c <= 'Z') || (c >= 'a' && c <= 'z')) {
	 		return true;
	 	}
	 	return false;
	 }
	 
	 /**
	 *	Checks if a character is a number or not.
	 *	@param c			Character to be checked
	 *	@return boolean		Returns true if the character is a number
	 *						Returns false if the character is not a number
	 */
	 private boolean isNumber(char c) {
	 	if (c >= '0' && c <= '9') {
	 		return true;
	 	}
	 	return false;
	 }
	 
	
	/**
	 *	Print the tokens in the array
	 *	Precondition: All elements in the array are valid String objects. (no nulls)
	 *	@param tokens	an array of String tokens
	 */
	public void printTokens(String[] tokens) {
		if (tokens == null) return;
		for (int a = 0; a < tokens.length; a++) {
			if (a % 5 == 0) System.out.print("\n  ");
			System.out.print("[token " + a + "]: " + tokens[a] + " ");
		}
		System.out.println();
	}

}