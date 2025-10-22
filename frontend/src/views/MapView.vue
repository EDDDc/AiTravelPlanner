<template>
  <section class="map-view">
    <header class="map-view__header">
      <div>
        <h1>地图视图</h1>
        <p>当前展示示例行程地点，后续将与实际行程数据联动。</p>
      </div>
      <button type="button" class="ghost-btn" @click="resetView">
        重置视角
      </button>
    </header>

    <div ref="mapContainer" class="map-view__container">
      <div v-if="loading" class="map-view__loading">地图加载中...</div>
      <div v-if="error" class="map-view__error">{{ error }}</div>
    </div>
  </section>
</template>

<script setup lang="ts">
import { onMounted, onUnmounted, ref } from "vue";
import { loadAmapSdk } from "../services/amapLoader";

type LngLatTuple = [number, number];

type MockMarker = {
  position: LngLatTuple;
  label: string;
};

const mapContainer = ref<HTMLDivElement | null>(null);
const loading = ref(true);
const error = ref("");
let mapInstance: AMap.Map | null = null;
let markers: AMap.Marker[] = [];

const mockPoints: MockMarker[] = [
  { position: [139.767052, 35.681167], label: "东京站" },
  { position: [139.699325, 35.689487], label: "新宿" },
  { position: [139.8107, 35.710063], label: "浅草寺" },
];

async function initMap() {
  if (!mapContainer.value) return;
  try {
    const AMap = await loadAmapSdk();
    mapInstance = new AMap.Map(mapContainer.value, {
      zoom: 12,
      center: mockPoints[0]?.position ?? [116.397428, 39.90923],
    });

    markers = mockPoints.map(
      ({ position, label }) =>
        new AMap.Marker({
          position,
          title: label,
          label: {
            content: label,
            direction: "top",
          },
        }),
    );

    mapInstance.add(markers);
    mapInstance.setFitView(markers);
  } catch (err) {
    error.value =
      err instanceof Error ? err.message : "加载高德地图失败，请检查配置。";
  } finally {
    loading.value = false;
  }
}

function resetView() {
  if (mapInstance && markers.length > 0) {
    mapInstance.setFitView(markers);
  }
}

onMounted(() => {
  initMap();
});

onUnmounted(() => {
  if (mapInstance && markers.length > 0) {
    mapInstance.remove(markers);
  }
  mapInstance?.destroy();
});
</script>

<style scoped>
.map-view {
  display: flex;
  flex-direction: column;
  gap: 16px;
  height: 100%;
}

.map-view__header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  gap: 24px;
}

.map-view__header h1 {
  margin: 0;
  font-size: 22px;
  font-weight: 600;
  color: #16213e;
}

.map-view__header p {
  margin: 6px 0 0;
  color: #6b7280;
}

.map-view__container {
  flex: 1;
  position: relative;
  min-height: 480px;
  border-radius: 16px;
  overflow: hidden;
  background: #eef2ff;
  box-shadow: inset 0 0 0 1px #e5e7eb;
}

.map-view__loading,
.map-view__error {
  position: absolute;
  top: 16px;
  left: 16px;
  background: rgba(255, 255, 255, 0.92);
  padding: 12px 16px;
  border-radius: 12px;
  box-shadow: 0 8px 20px -12px rgba(20, 36, 60, 0.2);
  color: #3b4262;
  font-size: 14px;
}

.map-view__error {
  color: #e11d48;
}

.ghost-btn {
  background: transparent;
  border: 1px solid #cbd5f5;
  border-radius: 10px;
  padding: 8px 14px;
  color: #3b52e6;
  cursor: pointer;
}
</style>
