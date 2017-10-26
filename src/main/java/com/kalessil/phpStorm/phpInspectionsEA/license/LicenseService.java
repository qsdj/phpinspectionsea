package com.kalessil.phpStorm.phpInspectionsEA.license;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.ui.LicensingFacade;
import com.kalessil.phpStorm.phpInspectionsEA.EAApplicationComponent;
import com.wyday.turboactivate.IsGenuineResult;
import com.wyday.turboactivate.TurboActivate;
import com.wyday.turboactivate.TurboActivateException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.*;
import java.util.HashMap;

final public class LicenseService {
    public boolean shouldCheckPluginLicense() {
        boolean result = false;
        if (!ApplicationManager.getApplication().isHeadlessEnvironment()) {
            final LicensingFacade facade = LicensingFacade.getInstance();
            result = facade != null && !facade.isEvaluationLicense();
        }
        return result;
    }

    @NotNull
    public TurboActivate getClient() throws IOException, URISyntaxException, TurboActivateException {
        final URL binaries = EAApplicationComponent.class.getResource("/TurboActivate/");
        if (binaries == null) {
            throw new RuntimeException("Licensing related resources are missing.");
        }

        final Path tempFolder        = Files.createTempDirectory("ea-ultimate-").toAbsolutePath();
        final String[] sourceDetails = binaries.toURI().toString().split("!");
        final FileSystem pluginJarFs = FileSystems.newFileSystem(URI.create(sourceDetails[0]), new HashMap<>());
        Files.walk(pluginJarFs.getPath(sourceDetails[1])).forEach(sourceFile -> {
            try {
                Files.copy(
                    sourceFile,
                    tempFolder.resolve(tempFolder.toString() + File.separator + sourceFile.toString()),
                    StandardCopyOption.COPY_ATTRIBUTES
                );
            } catch (Throwable copyFailure) {
                throw new RuntimeException(copyFailure);
            }
        });
        pluginJarFs.close();

        return new TurboActivate("2d65930359df9afb6f9a54.36732074", tempFolder.toString() + "/TurboActivate/");
    }

    /* Based on https://wyday.com/limelm/help/using-turboactivate-with-java/ */
    public boolean isActiveLicense(@NotNull TurboActivate client) throws TurboActivateException {
        final IsGenuineResult result = client.IsGenuine(90, 14, true, false);
        final boolean isGenuine      = result == IsGenuineResult.Genuine || result == IsGenuineResult.GenuineFeaturesChanged;
        /* positive when  check succeeded or network error occurred and license activated */
        return isGenuine || (result == IsGenuineResult.InternetError && client.IsActivated());
    }

    public boolean isTrialLicense(@NotNull TurboActivate client) throws TurboActivateException {
        //client.UseTrial(TurboActivate.TA_SYSTEM | TurboActivate.TA_VERIFIED_TRIAL, this.getLicenseHolder());
        //final int daysRemaining = client.TrialDaysRemaining(TurboActivate.TA_SYSTEM | TurboActivate.TA_VERIFIED_TRIAL);
        return false;
    }

    @Nullable
    private String getLicenseHolder() {
        String result = null;
        /* EAPs doesn't have license holder information */
        if (!ApplicationManager.getApplication().isEAP()) {
            final LicensingFacade facade = LicensingFacade.getInstance();
            result = facade == null ? null : facade.getLicensedToMessage();
        }
        return result;
    }
}