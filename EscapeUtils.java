public Class EscapeUtils {

    /**
   * Escapes all special characters in a JSON string according to RFC 8259
   * and JavaScript compatibility requirements.
   * 
   * @param input The raw string to be escaped
   * @return A JSON-escaped string suitable for embedding in JSON documents
   */
  public static String escapeJsonString(String input) {
      if (input == null) {
          return null;
      }
  
      StringBuilder sb = new StringBuilder(input.length() * 2);
      for (int i = 0; i < input.length(); i++) {
          char c = input.charAt(i);
          switch (c) {
              // Required escapes per JSON spec (RFC 8259)
              case '"':  sb.append("\\\""); break;
              case '\\': sb.append("\\\\"); break;
              case '\b': sb.append("\\b"); break;
              case '\f': sb.append("\\f"); break;
              case '\n': sb.append("\\n"); break;
              case '\r': sb.append("\\r"); break;
              case '\t': sb.append("\\t"); break;
              
              // Additional JavaScript/XML safety escapes
              case '\u2028': sb.append("\\u2028"); break;  // Line separator
              case '\u2029': sb.append("\\u2029"); break;  // Paragraph separator
              case '<':      sb.append("\\u003c"); break;  // Prevent </script>
              case '>':      sb.append("\\u003e"); break;  // Prevent -->
              
              // Control characters (U+0000 through U+001F)
              default:
                  if (c <= 0x1F) {
                      sb.append(String.format("\\u%04x", (int) c));
                  } else if (Character.isHighSurrogate(c) || Character.isLowSurrogate(c)) {
                      // Handle surrogate pairs for supplementary characters
                      if (Character.isHighSurrogate(c) && i + 1 < input.length() && 
                          Character.isLowSurrogate(input.charAt(i + 1))) {
                          sb.append(c);  // Keep valid surrogate pairs
                          sb.append(input.charAt(++i));
                      } else {
                          // Isolated surrogate - escape
                          sb.append(String.format("\\u%04x", (int) c));
                      }
                  } else {
                      sb.append(c);
                  }
                  break;
          }
      }
      return sb.toString();
  }

}
