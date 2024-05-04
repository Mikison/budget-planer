
/** @type {import('tailwindcss').Config} */
module.exports = {
  presets: [require('@spartan-ng/ui-core/hlm-tailwind-preset')],
  content: [
    './src/**/*.{html,ts}',
    './components/**/*.{html,ts}',
  ],
  darkMode: 'class',

  theme: {
    colors: {
      warning: '#FFC22DFF',
      success: '#00CA92FF',
      info: '#00B3F0FF',
      danger: '#FF6F70FF'
    },
    extend: {},
  },
  plugins: [],
};
