package com.github.razertexz;

import com.discord.utilities.textprocessing.DiscordParser;
import com.discord.utilities.textprocessing.Rules;
import com.discord.simpleast.core.parser.Parser;
import com.discord.simpleast.core.parser.Rule;

//import b.a.t.b.b.b;
import b.a.t.b.b.e;

import java.util.ArrayList;
import java.util.List;

public final class SimpleASTAPI {
    private static final List<Rule<?, ?, ?>> globalRules = new ArrayList<>();
    private final List<Rule<?, ?, ?>> registeredRules = new ArrayList<>();

    static {
        final var r = Rules.INSTANCE;

        /*globalRules.add(r.createSoftHyphenRule());
        globalRules.add(new b(e.h, e.f));
        globalRules.add(r.createBlockQuoteRule());
        globalRules.add(r.createCodeBlockRule());
        globalRules.add(r.createInlineCodeRule());
        globalRules.add(r.createSpoilerRule());
        globalRules.add(r.createMaskedLinkRule());
        globalRules.add(r.createUrlNoEmbedRule());
        globalRules.add(r.createUrlRule());
        globalRules.add(r.createCustomEmojiRule());
        globalRules.add(r.createNamedEmojiRule());
        globalRules.add(r.createUnescapeEmoticonRule());
        globalRules.add(r.createChannelMentionRule());
        globalRules.add(r.createRoleMentionRule());
        globalRules.add(r.createUserMentionRule());
        globalRules.add(r.createUnicodeEmojiRule());
        globalRules.add(r.createTimestampRule());
        globalRules.addAll(e.a(false, false));
        globalRules.add(r.createTextReplacementRule());*/

        try {
            final var rules = Parser.class.getDeclaredField("rules");
            rules.setAccessible(true);

            final var p1 = DiscordParser.class.getDeclaredField("SAFE_LINK_PARSER");
            p1.setAccessible(true);

            final var p2 = DiscordParser.class.getDeclaredField("MASKED_LINK_PARSER");
            p2.setAccessible(true);

            final var p3 = DiscordParser.class.getDeclaredField("FORUM_POST_FIRST_MESSAGE");
            p3.setAccessible(true);

            rules.set(p1.get(null), globalRules);
            rules.set(p2.get(null), globalRules);
            rules.set(p3.get(null), globalRules);
        } catch (Exception e) {}
    }

    public final void registerRule(final Rule<?, ?, ?> rule) {
        globalRules.add(rule);
        registeredRules.add(rule);
    }

    public final void unregisterRule(final Rule<?, ?, ?> rule) {
        globalRules.remove(rule);
        registeredRules.remove(rule);
    }

    public final void unregisterAll() {
        globalRules.removeAll(registeredRules);
        registeredRules.clear();
    }
}