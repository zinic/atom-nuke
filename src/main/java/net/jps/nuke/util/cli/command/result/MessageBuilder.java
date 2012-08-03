package net.jps.nuke.util.cli.command.result;

/**
 *
 * @author zinic
 */
public interface MessageBuilder {

    MessageBuilder append(double d);

    MessageBuilder append(float f);

    MessageBuilder append(long lng);

    MessageBuilder append(int i);

    MessageBuilder append(char c);

    MessageBuilder append(boolean b);

    MessageBuilder append(char[] str, int offset, int len);

    MessageBuilder append(char[] str);

    MessageBuilder append(CharSequence s, int start, int end);

    MessageBuilder append(CharSequence s);

    MessageBuilder append(StringBuffer sb);

    MessageBuilder append(String str);

    MessageBuilder append(Object obj);
}
