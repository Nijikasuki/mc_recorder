<template>
  <div class="flex bg-white" :style="{ height: 'calc(100vh - 56px)' }">
    <slot name="sidebar" />
    <slot name="main" />
  </div>
</template>

<script setup>
</script>
