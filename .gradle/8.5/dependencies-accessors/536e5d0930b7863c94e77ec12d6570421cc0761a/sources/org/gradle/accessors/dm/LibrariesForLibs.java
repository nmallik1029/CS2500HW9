package org.gradle.accessors.dm;

import org.gradle.api.NonNullApi;
import org.gradle.api.artifacts.MinimalExternalModuleDependency;
import org.gradle.plugin.use.PluginDependency;
import org.gradle.api.artifacts.ExternalModuleDependencyBundle;
import org.gradle.api.artifacts.MutableVersionConstraint;
import org.gradle.api.provider.Provider;
import org.gradle.api.model.ObjectFactory;
import org.gradle.api.provider.ProviderFactory;
import org.gradle.api.internal.catalog.AbstractExternalDependencyFactory;
import org.gradle.api.internal.catalog.DefaultVersionCatalog;
import java.util.Map;
import org.gradle.api.internal.attributes.ImmutableAttributesFactory;
import org.gradle.api.internal.artifacts.dsl.CapabilityNotationParser;
import javax.inject.Inject;

/**
 * A catalog of dependencies accessible via the `libs` extension.
 */
@NonNullApi
public class LibrariesForLibs extends AbstractExternalDependencyFactory {

    private final AbstractExternalDependencyFactory owner = this;
    private final JacksonLibraryAccessors laccForJacksonLibraryAccessors = new JacksonLibraryAccessors(owner);
    private final KotlinLibraryAccessors laccForKotlinLibraryAccessors = new KotlinLibraryAccessors(owner);
    private final KotlinxLibraryAccessors laccForKotlinxLibraryAccessors = new KotlinxLibraryAccessors(owner);
    private final Log4jLibraryAccessors laccForLog4jLibraryAccessors = new Log4jLibraryAccessors(owner);
    private final Slf4jLibraryAccessors laccForSlf4jLibraryAccessors = new Slf4jLibraryAccessors(owner);
    private final VersionAccessors vaccForVersionAccessors = new VersionAccessors(providers, config);
    private final BundleAccessors baccForBundleAccessors = new BundleAccessors(objects, providers, config, attributesFactory, capabilityNotationParser);
    private final PluginAccessors paccForPluginAccessors = new PluginAccessors(providers, config);

    @Inject
    public LibrariesForLibs(DefaultVersionCatalog config, ProviderFactory providers, ObjectFactory objects, ImmutableAttributesFactory attributesFactory, CapabilityNotationParser capabilityNotationParser) {
        super(config, providers, objects, attributesFactory, capabilityNotationParser);
    }

        /**
         * Creates a dependency provider for junit (junit:junit)
     * with version '4.13.2'.
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getJunit() {
            return create("junit");
    }

    /**
     * Returns the group of libraries at jackson
     */
    public JacksonLibraryAccessors getJackson() {
        return laccForJacksonLibraryAccessors;
    }

    /**
     * Returns the group of libraries at kotlin
     */
    public KotlinLibraryAccessors getKotlin() {
        return laccForKotlinLibraryAccessors;
    }

    /**
     * Returns the group of libraries at kotlinx
     */
    public KotlinxLibraryAccessors getKotlinx() {
        return laccForKotlinxLibraryAccessors;
    }

    /**
     * Returns the group of libraries at log4j
     */
    public Log4jLibraryAccessors getLog4j() {
        return laccForLog4jLibraryAccessors;
    }

    /**
     * Returns the group of libraries at slf4j
     */
    public Slf4jLibraryAccessors getSlf4j() {
        return laccForSlf4jLibraryAccessors;
    }

    /**
     * Returns the group of versions at versions
     */
    public VersionAccessors getVersions() {
        return vaccForVersionAccessors;
    }

    /**
     * Returns the group of bundles at bundles
     */
    public BundleAccessors getBundles() {
        return baccForBundleAccessors;
    }

    /**
     * Returns the group of plugins at plugins
     */
    public PluginAccessors getPlugins() {
        return paccForPluginAccessors;
    }

    public static class JacksonLibraryAccessors extends SubDependencyFactory {

        public JacksonLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

            /**
             * Creates a dependency provider for databind (com.fasterxml.jackson.core:jackson-databind)
         * with versionRef 'jackson'.
             * This dependency was declared in catalog libs.versions.toml
             */
            public Provider<MinimalExternalModuleDependency> getDatabind() {
                return create("jackson.databind");
        }

            /**
             * Creates a dependency provider for json (com.fasterxml.jackson.dataformat:jackson-dataformat-yaml)
         * with versionRef 'jackson'.
             * This dependency was declared in catalog libs.versions.toml
             */
            public Provider<MinimalExternalModuleDependency> getJson() {
                return create("jackson.json");
        }

    }

    public static class KotlinLibraryAccessors extends SubDependencyFactory {
        private final KotlinScriptLibraryAccessors laccForKotlinScriptLibraryAccessors = new KotlinScriptLibraryAccessors(owner);

        public KotlinLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

            /**
             * Creates a dependency provider for logging (io.github.microutils:kotlin-logging)
         * with version '3.0.4'.
             * This dependency was declared in catalog libs.versions.toml
             */
            public Provider<MinimalExternalModuleDependency> getLogging() {
                return create("kotlin.logging");
        }

        /**
         * Returns the group of libraries at kotlin.script
         */
        public KotlinScriptLibraryAccessors getScript() {
            return laccForKotlinScriptLibraryAccessors;
        }

    }

    public static class KotlinScriptLibraryAccessors extends SubDependencyFactory {

        public KotlinScriptLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

            /**
             * Creates a dependency provider for runtime (org.jetbrains.kotlin:kotlin-script-runtime)
         * with versionRef 'kotlin'.
             * This dependency was declared in catalog libs.versions.toml
             */
            public Provider<MinimalExternalModuleDependency> getRuntime() {
                return create("kotlin.script.runtime");
        }

    }

    public static class KotlinxLibraryAccessors extends SubDependencyFactory {
        private final KotlinxCoroutinesLibraryAccessors laccForKotlinxCoroutinesLibraryAccessors = new KotlinxCoroutinesLibraryAccessors(owner);

        public KotlinxLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Returns the group of libraries at kotlinx.coroutines
         */
        public KotlinxCoroutinesLibraryAccessors getCoroutines() {
            return laccForKotlinxCoroutinesLibraryAccessors;
        }

    }

    public static class KotlinxCoroutinesLibraryAccessors extends SubDependencyFactory {

        public KotlinxCoroutinesLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

            /**
             * Creates a dependency provider for core (org.jetbrains.kotlinx:kotlinx-coroutines-core)
         * with version '1.6.4'.
             * This dependency was declared in catalog libs.versions.toml
             */
            public Provider<MinimalExternalModuleDependency> getCore() {
                return create("kotlinx.coroutines.core");
        }

    }

    public static class Log4jLibraryAccessors extends SubDependencyFactory {

        public Log4jLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

            /**
             * Creates a dependency provider for core (org.apache.logging.log4j:log4j-core)
         * with versionRef 'log4j'.
             * This dependency was declared in catalog libs.versions.toml
             */
            public Provider<MinimalExternalModuleDependency> getCore() {
                return create("log4j.core");
        }

            /**
             * Creates a dependency provider for slf4j2 (org.apache.logging.log4j:log4j-slf4j2-impl)
         * with versionRef 'log4j'.
             * This dependency was declared in catalog libs.versions.toml
             */
            public Provider<MinimalExternalModuleDependency> getSlf4j2() {
                return create("log4j.slf4j2");
        }

    }

    public static class Slf4jLibraryAccessors extends SubDependencyFactory {

        public Slf4jLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

            /**
             * Creates a dependency provider for api (org.slf4j:slf4j-api)
         * with versionRef 'slf4j'.
             * This dependency was declared in catalog libs.versions.toml
             */
            public Provider<MinimalExternalModuleDependency> getApi() {
                return create("slf4j.api");
        }

            /**
             * Creates a dependency provider for nop (org.slf4j:slf4j-nop)
         * with versionRef 'slf4j'.
             * This dependency was declared in catalog libs.versions.toml
             */
            public Provider<MinimalExternalModuleDependency> getNop() {
                return create("slf4j.nop");
        }

            /**
             * Creates a dependency provider for simple (org.slf4j:slf4j-simple)
         * with versionRef 'slf4j'.
             * This dependency was declared in catalog libs.versions.toml
             */
            public Provider<MinimalExternalModuleDependency> getSimple() {
                return create("slf4j.simple");
        }

    }

    public static class VersionAccessors extends VersionFactory  {

        public VersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

            /**
             * Returns the version associated to this alias: jackson (2.17.2)
             * If the version is a rich version and that its not expressible as a
             * single version string, then an empty string is returned.
             * This version was declared in catalog libs.versions.toml
             */
            public Provider<String> getJackson() { return getVersion("jackson"); }

            /**
             * Returns the version associated to this alias: kotlin (1.8.0)
             * If the version is a rich version and that its not expressible as a
             * single version string, then an empty string is returned.
             * This version was declared in catalog libs.versions.toml
             */
            public Provider<String> getKotlin() { return getVersion("kotlin"); }

            /**
             * Returns the version associated to this alias: log4j (2.23.1)
             * If the version is a rich version and that its not expressible as a
             * single version string, then an empty string is returned.
             * This version was declared in catalog libs.versions.toml
             */
            public Provider<String> getLog4j() { return getVersion("log4j"); }

            /**
             * Returns the version associated to this alias: openrndr (0.4.2)
             * If the version is a rich version and that its not expressible as a
             * single version string, then an empty string is returned.
             * This version was declared in catalog libs.versions.toml
             */
            public Provider<String> getOpenrndr() { return getVersion("openrndr"); }

            /**
             * Returns the version associated to this alias: orml (0.4.1)
             * If the version is a rich version and that its not expressible as a
             * single version string, then an empty string is returned.
             * This version was declared in catalog libs.versions.toml
             */
            public Provider<String> getOrml() { return getVersion("orml"); }

            /**
             * Returns the version associated to this alias: orx (0.4.2)
             * If the version is a rich version and that its not expressible as a
             * single version string, then an empty string is returned.
             * This version was declared in catalog libs.versions.toml
             */
            public Provider<String> getOrx() { return getVersion("orx"); }

            /**
             * Returns the version associated to this alias: slf4j (2.0.16)
             * If the version is a rich version and that its not expressible as a
             * single version string, then an empty string is returned.
             * This version was declared in catalog libs.versions.toml
             */
            public Provider<String> getSlf4j() { return getVersion("slf4j"); }

    }

    public static class BundleAccessors extends BundleFactory {

        public BundleAccessors(ObjectFactory objects, ProviderFactory providers, DefaultVersionCatalog config, ImmutableAttributesFactory attributesFactory, CapabilityNotationParser capabilityNotationParser) { super(objects, providers, config, attributesFactory, capabilityNotationParser); }

    }

    public static class PluginAccessors extends PluginFactory {
        private final GitarchivePluginAccessors paccForGitarchivePluginAccessors = new GitarchivePluginAccessors(providers, config);
        private final KotlinPluginAccessors paccForKotlinPluginAccessors = new KotlinPluginAccessors(providers, config);

        public PluginAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

            /**
             * Creates a plugin provider for dokka to the plugin id 'org.jetbrains.dokka'
             * with version '1.9.20'.
             * This plugin was declared in catalog libs.versions.toml
             */
            public Provider<PluginDependency> getDokka() { return createPlugin("dokka"); }

            /**
             * Creates a plugin provider for runtime to the plugin id 'org.beryx.runtime'
             * with version '1.13.0'.
             * This plugin was declared in catalog libs.versions.toml
             */
            public Provider<PluginDependency> getRuntime() { return createPlugin("runtime"); }

            /**
             * Creates a plugin provider for shadow to the plugin id 'com.gradleup.shadow'
             * with version '8.3.0'.
             * This plugin was declared in catalog libs.versions.toml
             */
            public Provider<PluginDependency> getShadow() { return createPlugin("shadow"); }

            /**
             * Creates a plugin provider for versions to the plugin id 'com.github.ben-manes.versions'
             * with version '0.46.0'.
             * This plugin was declared in catalog libs.versions.toml
             */
            public Provider<PluginDependency> getVersions() { return createPlugin("versions"); }

        /**
         * Returns the group of plugins at plugins.gitarchive
         */
        public GitarchivePluginAccessors getGitarchive() {
            return paccForGitarchivePluginAccessors;
        }

        /**
         * Returns the group of plugins at plugins.kotlin
         */
        public KotlinPluginAccessors getKotlin() {
            return paccForKotlinPluginAccessors;
        }

    }

    public static class GitarchivePluginAccessors extends PluginFactory {

        public GitarchivePluginAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

            /**
             * Creates a plugin provider for gitarchive.tomarkdown to the plugin id 'org.openrndr.extra.gitarchiver.tomarkdown'
             * with versionRef 'orx'.
             * This plugin was declared in catalog libs.versions.toml
             */
            public Provider<PluginDependency> getTomarkdown() { return createPlugin("gitarchive.tomarkdown"); }

    }

    public static class KotlinPluginAccessors extends PluginFactory {

        public KotlinPluginAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

            /**
             * Creates a plugin provider for kotlin.jvm to the plugin id 'org.jetbrains.kotlin.jvm'
             * with versionRef 'kotlin'.
             * This plugin was declared in catalog libs.versions.toml
             */
            public Provider<PluginDependency> getJvm() { return createPlugin("kotlin.jvm"); }

    }

}
