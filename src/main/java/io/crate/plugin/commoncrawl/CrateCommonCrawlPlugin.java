package io.crate.plugin.commoncrawl;

import com.google.common.collect.Lists;
import io.crate.plugin.AbstractPlugin;
import org.elasticsearch.common.inject.Module;
import org.elasticsearch.common.settings.Settings;

import java.util.Collection;
import java.util.List;

public class CrateCommonCrawlPlugin extends AbstractPlugin {


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
    public Collection<Module> nodeModules() {
        return Lists.<Module>newArrayList(new CommonCrawlModule());
    }
}