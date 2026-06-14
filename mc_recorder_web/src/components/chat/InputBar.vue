<template>
  <div class="flex items-end gap-2 rounded-xl border border-gray-200/80 bg-white p-2 shadow-sm transition-all duration-200 focus-within:border-indigo-300 focus-within:shadow-md focus-within:ring-2 focus-within:ring-indigo-100">
    <textarea
      ref="textareaRef"
      :value="modelValue"
      @input="$emit('update:modelValue', $event.target.value)"
      @keydown.enter.exact.prevent="handleEnter"
      @keydown.enter.shift.exact="() => {}"
      :disabled="loading"
      placeholder="问点什么... (Enter 发送, Shift+Enter 换行)"
      rows="1"
      class="flex-1 resize-none bg-transparent px-2 py-1.5 text-[14px] text-gray-900 placeholder-gray-400 outline-none disabled:opacity-50"
      style="max-height: 180px"
    ></textarea>

    <button
      @click="$emit('send')"
      :disabled="loading || !modelValue.trim()"
      class="flex h-8 w-8 flex-shrink-0 items-center justify-center rounded-lg bg-gray-900 text-white transition-all duration-200 hover:bg-gray-800 active:scale-95 disabled:cursor-not-allowed disabled:bg-gray-200 disabled:text-gray-400"
      title="发送 (Enter)"
    >
      <svg v-if="!loading" class="h-4 w-4" fill="none" viewBox="0 0 24 24" stroke="currentColor" stroke-width="2.5">
        <path stroke-linecap="round" stroke-linejoin="round" d="M5 10l7-7m0 0l7 7m-7-7v18" />
      </svg>
      <svg v-else class="h-4 w-4 animate-spin" fill="none" viewBox="0 0 24 24">
        <circle cx="12" cy="12" r="10" stroke="currentColor" stroke-width="3" stroke-opacity="0.25"></circle>
        <path fill="currentColor" d="M4 12a8 8 0 018-8v3a5 5 0 00-5 5H4z"></path>
      </svg>
    </button>
  </div>
</template>

<script setup>
import { ref, watch, nextTick } from 'vue'

const props = defineProps({
  modelValue: { type: String, default: '' },
  loading: { type: Boolean, default: false },
})
const emit = defineEmits(['update:modelValue', 'send'])

const textareaRef = ref(null)

function handleEnter() {
  if (props.modelValue.trim() && !props.loading) emit('send')
}

watch(() => props.modelValue, async () => {
  await nextTick()
  if (textareaRef.value) {
    textareaRef.value.style.height = 'auto'
    textareaRef.value.style.height = Math.min(textareaRef.value.scrollHeight, 180) + 'px'
  }
})
</script>
