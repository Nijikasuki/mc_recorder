<template>
  <div class="relative flex items-end gap-2 rounded-2xl border border-white/10 bg-white/[0.03] p-2 backdrop-blur-xl transition-all duration-300 focus-within:border-blue-400/40 focus-within:shadow-xl focus-within:shadow-blue-500/10">
    <textarea
      ref="textareaRef"
      :value="modelValue"
      @input="$emit('update:modelValue', $event.target.value)"
      @keydown.enter.exact.prevent="handleEnter"
      @keydown.enter.shift.exact="() => {}"
      :disabled="loading"
      placeholder="问点什么... (Enter 发送, Shift+Enter 换行)"
      rows="1"
      class="flex-1 resize-none bg-transparent px-3 py-2 text-sm text-gray-100 placeholder-gray-500 outline-none disabled:opacity-50"
      style="max-height: 200px"
    ></textarea>

    <button
      @click="$emit('send')"
      :disabled="loading || !modelValue.trim()"
      class="group flex h-10 w-10 flex-shrink-0 items-center justify-center rounded-xl bg-gradient-to-br from-blue-500 to-purple-500 text-white shadow-lg shadow-blue-500/30 transition-all duration-300 ease-out hover:scale-105 hover:shadow-xl hover:shadow-purple-500/40 active:scale-95 disabled:cursor-not-allowed disabled:opacity-40 disabled:shadow-none disabled:hover:scale-100"
      title="发送 (Enter)"
    >
      <svg
        v-if="!loading"
        class="h-5 w-5 transition-transform duration-300 group-hover:translate-x-0.5"
        fill="none"
        viewBox="0 0 24 24"
        stroke="currentColor"
        stroke-width="2.5"
      >
        <path stroke-linecap="round" stroke-linejoin="round" d="M14 5l7 7m0 0l-7 7m7-7H3" />
      </svg>
      <svg
        v-else
        class="h-5 w-5 animate-spin"
        fill="none"
        viewBox="0 0 24 24"
      >
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
  if (props.modelValue.trim() && !props.loading) {
    emit('send')
  }
}

// 自动调整高度
watch(() => props.modelValue, async () => {
  await nextTick()
  if (textareaRef.value) {
    textareaRef.value.style.height = 'auto'
    textareaRef.value.style.height = Math.min(textareaRef.value.scrollHeight, 200) + 'px'
  }
})
</script>
