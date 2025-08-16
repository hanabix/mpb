import { defineConfig } from "vite";
import webExtension, { readJsonFile } from "vite-plugin-web-extension";

function generateManifest() {
  const manifest = readJsonFile("manifest.json");
  const pkg = readJsonFile("package.json");
  return {
    name: pkg.name,
    description: pkg.description,
    version: pkg.version,
    ...manifest,
  };
}

export default defineConfig(({ mode }) => {
  return {
    resolve: {
      alias: [{ find: "src", replacement: "useless", customResolver: () => `out/${mode === 'development' ? 'fastLinkJS' : 'fullLinkJS'}.dest/main.js` }]
    },
    optimizeDeps: {
      entries: [],
    },
    plugins: [
      webExtension({
        manifest: generateManifest,
        watchFilePaths: ["package.json", "manifest.json"],
        webExtConfig: {
          target: "chromium",
          startUrl: ["https://connect.garmin.cn/modern/activities?activityType=running"],
          watchFile: ["content.js", "manifest.json"],
        },
      }),
    ],
  };
});
