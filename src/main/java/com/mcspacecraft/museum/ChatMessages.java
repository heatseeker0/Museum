package com.mcspacecraft.museum;

import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidParameterException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.minimessage.MiniMessage;

public class ChatMessages {
    private final static MiniMessage MINIMESSAGE = MiniMessage.builder().strict(true).build();
    private final static Component DEFAULT_ERROR_MESSAGE = Component.text("This key doesn't exist in messages.properties", Style.style(NamedTextColor.RED));
    private final static Pattern ARG_PATTERN = Pattern.compile("\\{([a-zA-Z0-9_]+)(?>,[^}]*)*}");

    private final static Map<String, Component> messages = new HashMap<>();

    public static void loadPluginMessages(ClassLoader classLoader) {
        try (InputStream translations = classLoader.getResourceAsStream("messages.properties")) {
            if (translations == null) {
                return;
            }

            Properties properties = new Properties();
            properties.load(translations);
            properties.entrySet().forEach(e -> {
                messages.put((String) e.getKey(), MINIMESSAGE.deserialize((String) e.getValue()));
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Component getMessage(String key) {
        return messages.getOrDefault(key, DEFAULT_ERROR_MESSAGE);
    }

    public static Component getMessage(String key, Object... args) {
        if (args.length == 0) {
            return getMessage(key);
        }
        
        if (args.length % 2 != 0) {
            throw new InvalidParameterException("Messages#getMessage called with odd number of replacement args.");
        }
        
        // <argument_name, argument_value>
        Map<String, String> replacements = new HashMap<>();
        for (int i = 0; i < args.length / 2; i += 2) {
            replacements.put(args[i].toString(), args[i+1].toString());
        }

        String input = MINIMESSAGE.serialize(getMessage(key));
        
        int offset = 0;

        StringBuilder output = new StringBuilder(input);
        Matcher matcher = ARG_PATTERN.matcher(input);
        
        while (matcher.find()) {
            String arg = matcher.group(1);

            String text = replacements.getOrDefault(arg, arg);
            
            output.replace(matcher.start() + offset, matcher.end() + offset, text);
            
            offset += text.length() - matcher.group().length();
        }
        
        return MINIMESSAGE.deserialize(output.toString());
    }
}
