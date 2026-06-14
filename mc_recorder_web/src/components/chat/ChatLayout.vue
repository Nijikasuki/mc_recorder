<template>
  <div class="flex bg-[#0D1117]" :style="{ height: 'calc(100vh - 56px)' }">
    <slot name="sidebar" />
    <slot name="main" />
  </div>
</template>
