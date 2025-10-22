const AMAP_SDK_URL = "https://webapi.amap.com/maps?v=2.0";

declare global {
  interface Window {
    AMap?: typeof AMap;
    _amapLoaderPromise?: Promise<typeof AMap>;
  }
}

export function loadAmapSdk(): Promise<typeof AMap> {
  if (window._amapLoaderPromise) {
    return window._amapLoaderPromise;
  }

  window._amapLoaderPromise = new Promise((resolve, reject) => {
    if (window.AMap) {
      resolve(window.AMap);
      return;
    }

    const apiKey = import.meta.env.VITE_APP_AMAP_WEB_KEY;
    if (!apiKey) {
      reject(new Error("未配置 VITE_APP_AMAP_WEB_KEY，无法加载高德地图 SDK。"));
      return;
    }

    const script = document.createElement("script");
    script.src = `${AMAP_SDK_URL}&key=${apiKey}`;
    script.async = true;
    script.onerror = (event) => {
      reject(new Error(`高德地图 SDK 加载失败: ${String(event)}`));
    };
    script.onload = () => {
      if (window.AMap) {
        resolve(window.AMap);
      } else {
        reject(new Error("高德地图 SDK 加载完成但未找到 AMap 对象"));
      }
    };

    document.head.appendChild(script);
  });

  return window._amapLoaderPromise;
}
