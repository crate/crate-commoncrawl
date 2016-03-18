package io.crate.plugin.commoncrawl;

import com.google.common.collect.Lists;
import io.crate.plugin.AbstractPlugin;
import org.elasticsearch.common.inject.Module;
import org.elasticsearch.common.settings.Settings;

import java.util.Collection;
import java.util.List;

public class CrateCommonCrawlPlugin extends AbstractPlugin {


    public CrateCommonCrawlPlugin(Settings settings) {

    }

    @Override
    public String name() {
        return "crate-commoncrawl";
    }

    @Override
    public String description() {

        return "Plugin to import common crawl data form http endpoints";
    }

    @Override
    public Collection<Module> modules(Settings settings) {
        List<Module> modules = Lists.newArrayList(super.modules(settings));
        modules.add(new CommonCrawlModule());
        return modules;
    }
}