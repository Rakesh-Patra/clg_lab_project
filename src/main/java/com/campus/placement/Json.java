package com.campus.placement;

import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.util.*;

/** Small JSON encoder/parser for the app's local HTTP API; no third-party library required. */
final class Json {
    private Json() {}
    static String stringify(Object value) {
        if(value==null)return "null";
        if(value instanceof String||value instanceof Character)return quote(value.toString());
        if(value instanceof Number||value instanceof Boolean)return value.toString();
        if(value instanceof Map<?,?> m){StringJoiner j=new StringJoiner(",","{","}");for(var e:m.entrySet())j.add(quote(String.valueOf(e.getKey()))+":"+stringify(e.getValue()));return j.toString();}
        if(value instanceof Iterable<?> it){StringJoiner j=new StringJoiner(",","[","]");for(Object v:it)j.add(stringify(v));return j.toString();}
        if(value.getClass().isArray()){StringJoiner j=new StringJoiner(",","[","]");for(int i=0;i<Array.getLength(value);i++)j.add(stringify(Array.get(value,i)));return j.toString();}
        return quote(value.toString());
    }
    private static String quote(String s){StringBuilder b=new StringBuilder("\"");for(char c:s.toCharArray()){switch(c){case '"'->b.append("\\\"");case '\\'->b.append("\\\\");case '\b'->b.append("\\b");case '\f'->b.append("\\f");case '\n'->b.append("\\n");case '\r'->b.append("\\r");case '\t'->b.append("\\t");default->{if(c<32)b.append(String.format("\\u%04x",(int)c));else b.append(c);}}}return b.append('"').toString();}
    static Object parse(String s){Parser p=new Parser(s);Object v=p.value();p.ws();if(p.i!=s.length())throw new IllegalArgumentException("Invalid JSON.");return v;}
    private static final class Parser {
        final String s;int i;Parser(String s){this.s=s;}void ws(){while(i<s.length()&&Character.isWhitespace(s.charAt(i)))i++;}
        Object value(){ws();if(i>=s.length())throw bad();char c=s.charAt(i);if(c=='{')return object();if(c=='[')return array();if(c=='"')return str();if(c=='t'&&take("true"))return true;if(c=='f'&&take("false"))return false;if(c=='n'&&take("null"))return null;return number();}
        boolean take(String x){if(s.startsWith(x,i)){i+=x.length();return true;}return false;}
        Map<String,Object> object(){i++;Map<String,Object>m=new LinkedHashMap<>();ws();if(ch('}'))return m;while(true){ws();if(i>=s.length()||s.charAt(i)!='"')throw bad();String k=str();ws();need(':');m.put(k,value());ws();if(ch('}'))return m;need(',');}}
        List<Object> array(){i++;List<Object>a=new ArrayList<>();ws();if(ch(']'))return a;while(true){a.add(value());ws();if(ch(']'))return a;need(',');}}
        String str(){need('"');StringBuilder b=new StringBuilder();while(i<s.length()){char c=s.charAt(i++);if(c=='"')return b.toString();if(c=='\\'){if(i>=s.length())throw bad();char e=s.charAt(i++);switch(e){case '"','\\','/'->b.append(e);case 'b'->b.append('\b');case 'f'->b.append('\f');case 'n'->b.append('\n');case 'r'->b.append('\r');case 't'->b.append('\t');case 'u'->{if(i+4>s.length())throw bad();try{b.append((char)Integer.parseInt(s.substring(i,i+4),16));}catch(NumberFormatException ex){throw bad();}i+=4;}default->throw bad();}}else{if(c<32)throw bad();b.append(c);}}throw bad();}
        Object number(){int start=i;if(ch('-')){}while(i<s.length()&&Character.isDigit(s.charAt(i)))i++;if(ch('.'))while(i<s.length()&&Character.isDigit(s.charAt(i)))i++;if(i<s.length()&&(s.charAt(i)=='e'||s.charAt(i)=='E')){i++;if(i<s.length()&&(s.charAt(i)=='+'||s.charAt(i)=='-'))i++;while(i<s.length()&&Character.isDigit(s.charAt(i)))i++;}if(start==i)throw bad();try{return new BigDecimal(s.substring(start,i));}catch(NumberFormatException e){throw bad();}}
        boolean ch(char c){if(i<s.length()&&s.charAt(i)==c){i++;return true;}return false;}void need(char c){if(!ch(c))throw bad();}IllegalArgumentException bad(){return new IllegalArgumentException("Invalid JSON.");}
    }
}
