package io.crate.plugin.commoncrawl;

import io.crate.Plugin;
import org.elasticsearch.common.inject.Module;
import org.elasticsearch.common.settings.Settings;

import java.util.Collection;
import java.util.Collections;

public class CrateCommonCrawlPlugin implements Plugin {


    public static final String PLUGIN_NAME = "crate-commoncrawl";
    public static final String PLUGIN_DESC = "Plugin to import common crawl data form http endpoints";


    public CrateCommonCrawlPlugin(Settings settings) {

    }

    @Override
    public String name() {
        return PLUGIN_NAME;
    }

    @Override
    public String description() {
        return PLUGIN_DESC;
    }

    @Override
    public Collection<Module> createGuiceModules() {
        return Collections.singletonList(new CommonCrawlModule());
    }
}