/// <reference types="vitest" />
/// <reference types="vite/client" />

import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'
import path, { resolve } from 'path'

// https://vitejs.dev/config/
export default defineConfig({
  plugins: [react()],
    test: {
      environment: 'jsdom',
      setupFiles: [resolve(__dirname, 'tests/setup/setup.ts')],
    }
})