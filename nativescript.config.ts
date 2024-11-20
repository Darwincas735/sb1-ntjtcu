import { NativeScriptConfig } from '@nativescript/core';

export default {
  id: 'org.nativescript.censusapp',
  appPath: 'app',
  appResourcesPath: 'App_Resources',
  android: {
    v8Flags: '--expose_gc',
    markingMode: 'none',
    codeCache: true,
    maxLogcatObjectSize: 2048,
    discardUncaughtJsExceptions: true
  }
} as NativeScriptConfig;