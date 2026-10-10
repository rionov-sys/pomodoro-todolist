---
name: Grinding Productivity System
colors:
  surface: '#f7f9fb'
  surface-dim: '#d8dadc'
  surface-bright: '#f7f9fb'
  surface-container-lowest: '#ffffff'
  surface-container-low: '#f2f4f6'
  surface-container: '#eceef0'
  surface-container-high: '#e6e8ea'
  surface-container-highest: '#e0e3e5'
  on-surface: '#191c1e'
  on-surface-variant: '#3c494a'
  inverse-surface: '#2d3133'
  inverse-on-surface: '#eff1f3'
  outline: '#6c797b'
  outline-variant: '#bbc9ca'
  surface-tint: '#006970'
  primary: '#006970'
  on-primary: '#ffffff'
  primary-container: '#14b8c4'
  on-primary-container: '#004348'
  inverse-primary: '#4ed9e5'
  secondary: '#565e74'
  on-secondary: '#ffffff'
  secondary-container: '#dae2fd'
  on-secondary-container: '#5c647a'
  tertiary: '#5e5c6e'
  on-tertiary: '#ffffff'
  tertiary-container: '#a7a4b7'
  on-tertiary-container: '#3b3a4a'
  error: '#ba1a1a'
  on-error: '#ffffff'
  error-container: '#ffdad6'
  on-error-container: '#93000a'
  primary-fixed: '#7ff4ff'
  primary-fixed-dim: '#4ed9e5'
  on-primary-fixed: '#002022'
  on-primary-fixed-variant: '#004f55'
  secondary-fixed: '#dae2fd'
  secondary-fixed-dim: '#bec6e0'
  on-secondary-fixed: '#131b2e'
  on-secondary-fixed-variant: '#3f465c'
  tertiary-fixed: '#e4e0f5'
  tertiary-fixed-dim: '#c7c4d8'
  on-tertiary-fixed: '#1b1a29'
  on-tertiary-fixed-variant: '#464555'
  background: '#f7f9fb'
  on-background: '#191c1e'
  surface-variant: '#e0e3e5'
typography:
  display-lg:
    fontFamily: Plus Jakarta Sans
    fontSize: 48px
    fontWeight: '700'
    lineHeight: 56px
    letterSpacing: -0.02em
  display-timer:
    fontFamily: Plus Jakarta Sans
    fontSize: 64px
    fontWeight: '800'
    lineHeight: 72px
    letterSpacing: -0.03em
  headline-lg:
    fontFamily: Plus Jakarta Sans
    fontSize: 30px
    fontWeight: '700'
    lineHeight: 38px
    letterSpacing: -0.01em
  headline-md:
    fontFamily: Plus Jakarta Sans
    fontSize: 24px
    fontWeight: '600'
    lineHeight: 32px
  headline-sm:
    fontFamily: Plus Jakarta Sans
    fontSize: 20px
    fontWeight: '600'
    lineHeight: 28px
  body-lg:
    fontFamily: Plus Jakarta Sans
    fontSize: 16px
    fontWeight: '400'
    lineHeight: 24px
  body-md:
    fontFamily: Plus Jakarta Sans
    fontSize: 14px
    fontWeight: '400'
    lineHeight: 20px
  body-sm:
    fontFamily: Plus Jakarta Sans
    fontSize: 12px
    fontWeight: '400'
    lineHeight: 18px
  label-lg:
    fontFamily: Plus Jakarta Sans
    fontSize: 14px
    fontWeight: '600'
    lineHeight: 20px
    letterSpacing: 0.01em
  label-md:
    fontFamily: Plus Jakarta Sans
    fontSize: 12px
    fontWeight: '600'
    lineHeight: 16px
    letterSpacing: 0.02em
  label-sm:
    fontFamily: Plus Jakarta Sans
    fontSize: 11px
    fontWeight: '500'
    lineHeight: 14px
    letterSpacing: 0.02em
rounded:
  sm: 0.5rem
  DEFAULT: 1rem
  md: 1.5rem
  lg: 2rem
  xl: 3rem
  full: 9999px
spacing:
  gutter: 1rem
  gutter-mobile: 0.75rem
  margin: 1rem
  margin-mobile: 1rem
  margin-tablet: 1.5rem
  space-xs: 0.25rem
  space-sm: 0.5rem
  space-md: 1rem
  space-lg: 1.5rem
  space-xl: 2rem
---

## Brand & Style

Sistem desain ini ditujukan untuk aplikasi produktivitas mobile Android yang memadukan teknik Pomodoro dan manajemen tugas harian. Pendekatan visual berakar pada evolusi modern Material 3 (Material You) dengan sentuhan kontemporer: fungsional, bersih, ramah, dan bebas distraksi visual (*calm productivity*).

### Persona & Nilai Visual
- **Fokus & Jernih:** Menghilangkan kelelahan visual (*visual fatigue*) lewat kanvas putih terang, tipografi tegas berwarna navy pekat, serta ritme tata letak yang lapang.
- **Ramah & Terorganisir:** Kartu tugas menggunakan kontainer pastel lembut bergradasi tonal untuk menandai kategori atau status tanpa menimbulkan stres kognitif.
- **Tegas & Terarah:** Tombol aksi utama (*primary CTA*) berbentuk kapsul tegas (*sleek pill*) dengan kontras tinggi guna memperjelas alur kerja sesi fokus (*Pomodoro*) dan penyelesaian target harian.
- **Bahasa Antarmuka:** Bahasa Indonesia lugas, terstruktur, dan memotivasi (misalnya: *Mulai Sesi*, *Tugas Selesai*, *Istirahat Singkat*).

## Colors

Palet warna dibangun di atas landasan Material 3 berbasis light mode murni yang mengedepankan legibilitas dan pemisahan informasi berbasis peran warna.

### Warna Utama & Struktur UI
- **Primary Teal (`#14B8C4`):** Menggambarkan energi fokus, kemajuan timer Pomodoro, status aktif, dan aksen interaktif utama.
- **Deep Navy Text / Primary Surface Action (`#0F172A` & `#1E293B`):** Warna kontras tinggi untuk tombol CTA utama, teks judul dominan, dan ikon navigasi aktif.
- **Muted Slate (`#64748B`):** Digunakan untuk label pelengkap, placeholder, waktu pengerjaan tugas, dan ikon sekunder.
- **Kanvas Dasar (`#FFFFFF`, `#F8FAFC`):** `#F8FAFC` berfungsi sebagai `surface-container-lowest` untuk latar belakang utama aplikasi, sedangkan `#FFFFFF` digunakan untuk surface elevated.

### Palet Pastel Task Container
Kategori tugas diorganisir menggunakan kontainer bernuansa pastel ramah mata yang dipadukan dengan teks navy pekat (`#0F172A` / `#1E293B`):
- **Pastel Pink (`#FEE2E2`):** Kategori Mendesak / Prioritas Tinggi (*Penting*).
- **Pastel Lavender (`#EDE9FE`):** Kategori Belajar & Riset (*Belajar*).
- **Pastel Mint (`#DCFCE7`):** Kategori Selesai / Rutinitas Pribadi (*Kesehatan & Hobi*).
- **Pastel Amber (`#FEF3C7`):** Kategori Menengah / Dalam Proses (*Pekerjaan*).
- **Pastel Sky (`#E0F2FE`):** Kategori Sesi Fokus Standar (*Tugas Harian*).

## Typography

Sistem tipografi menggunakan **Plus Jakarta Sans** di seluruh tingkatan antarmuka. Karakter font ini menyediakan geometris modern yang hangat, kurva humanist yang ramah, serta angka tabular yang kokoh untuk penunjuk waktu digital Pomodoro.

### Penerapan Peran
- **Display Timer (`64px / 800`):** Khusus dialokasikan untuk hitung mundur durasi Pomodoro (*25:00*, *05:00*), memberikan penegasan fokus instan saat ponsel diletakkan di meja kerja.
- **Headline (Lg, Md, Sm):** Mengatur hirarki halaman seperti salam pembuka, judul modal input tugas, dan ringkasan statistik harian.
- **Body Text:** Menggunakan bobot reguler (*400*) dengan kontras warna `#1E293B` untuk deskripsi tugas agar tetap nyaman dibaca dalam kondisi pencahayaan rendah maupun terik.
- **Label & Aksi:** Menggunakan bobot semi-bold (*600*) untuk tombol navigasi, chip kategori, indikator siklus Pomodoro, serta microcopy pendukung.

## Layout & Spacing

Sistem tata letak mengikuti arsitektur kolom adaptif Material 3 dengan batas ergonomis layar sentuh ponsel Android.

### Grid & Ergonomi Mobile
- **Struktur Kolom:** 4 kolom untuk perangkat mobile standar (< 600dp) dengan margin luar `1rem` (16px) dan gutter `0.75rem` (12px). Untuk layar tablet dan lipat (> 600dp), beralih ke 8 kolom dengan margin luar `1.5rem` (24px).
- **Zona Jangkauan Jempol (*Thumb Zone*):** Aksi terpenting seperti tombol *Mulai Sesi* dan tombol *Tambah Tugas Baru* ditempatkan pada sepertiga area bawah layar.
- **Ritme Spasi Vertikal:**
  - Jarak antar item dalam daftar tugas: `space-sm` (8px).
  - Jarak antar seksi (misal antara Timer Pomodoro dan Daftar Tugas Hari Ini): `space-lg` (24px) hingga `space-xl` (32px).
  - Padding internal dalam kartu tugas: `space-md` (16px).

## Elevation & Depth

Pendekatan elevasi mengutamakan *tonal layering* khas Material 3 dan meminimalkan bayangan tebal guna menjaga kemurnian kanvas putih.

### Tingkatan Lapisan Visual
- **Level 0 (Surface Base - `#F8FAFC`):** Kanvas latar belakang utama aplikasi tempat seluruh modul diletakkan.
- **Level 1 (Surface Containers - Pastel & Pure White):** Kartu tugas pastel dan kontainer ringkasan statistik. Tanpa bayangan; kedalaman dibentuk murni melalui perbedaan hue warna terhadap background.
- **Level 2 (Active Focus & Sheets):** Bottom sheet pengeditan tugas, dialog pengaturan durasi, dan panel timer aktif. Menggunakan bayangan difus ultra-halus:
  - `box-shadow: 0 8px 24px -4px rgba(15, 23, 42, 0.06), 0 2px 6px -2px rgba(15, 23, 42, 0.04)`
- **Level 3 (Floating Actions & Sticky Timer Bar):** Tombol aksi melayang (FAB) dan bilah navigasi bawah (*Bottom Navigation Bar*). Dilengkapi bayangan halus beraksen navy redup untuk mempertahankan pemisahan saat pengguna menggulir daftar tugas:
  - `box-shadow: 0 12px 32px -6px rgba(15, 23, 42, 0.10)`

## Shapes

Sistem bentuk didasarkan pada tingkat kelengkungan organik tinggi (Pill / Round Level 3) yang mencerminkan sifat ramah, dinamis, dan modern khas Android masa kini.

### Standar Kurvatur
- **Tombol & Aksi Utama (Pill - Full Rounded):** Tombol CTA utama, tombol putar/jeda timer, dan search bar menggunakan radius `9999px` (kapsul penuh).
- **Kartu Tugas Pastel:** Radius sudut `1.25rem` (20px) hingga `1.5rem` (24px), memberikan siluet kontainer yang lembut, tidak kaku, dan mudah dikenali secara visual.
- **Modal & Bottom Sheet:** Sudut atas membulat dengan radius `1.75rem` (28px).
- **Chip & Tag Kategori:** Radius `9999px` untuk mempertahankan konsistensi bentuk kapsul mini.

## Components

### 1. Tombol (Buttons)
- **Primary CTA (Mulai Fokus / Tambah Tugas):** Berbentuk pill penuh (`rounded-full`), latar belakang Navy Gelap (`#0F172A`) dengan teks putih tebal (`#FFFFFF`), atau Teal (`#14B8C4`) dengan teks navy pekat saat menandakan fase timer aktif. Padding vertikal `14px`, horizontal `28px`.
- **Secondary Action (Jeda / Batalkan):** Bentuk pill dengan latar belakang transparan bergaris tepi halus (`1.5px solid #E2E8F0`) atau tonal neutral (`#E2E8F0` dengan teks `#1E293B`).
- **Icon Floating Button (FAB):** Berbentuk squircle/lingkaran penuh dengan diameter 56px, latar Teal (`#14B8C4`), ikon putih 24px.

### 2. Kartu Tugas (Task Cards)
- Setiap kartu memiliki background pastel sesuai kategori (misal: `#EDE9FE` untuk *Belajar*, `#FEE2E2` untuk *Penting*).
- Padding internal `16px`, sudut membulat `20px`.
- Tata letak internal:
  - Kiri: Checkbox bulat kustom.
  - Tengah: Judul tugas (`headline-sm` / `body-lg` warna `#0F172A`), diikuti metadata waktu dan estimasi siklus Pomodoro (misal: *🍅 2/4 Siklus • 14:00*) berwarna `#475569`.
  - Kanan: Chip kategori ringkas atau ikon menu aksi cepat.
- Ketika tugas dicentang selesai, kartu bertransisi dengan opasitas `0.6` dan judul tugas tercoret (*strikethrough*).

### 3. Checkbox & Radio Button
- **Checkbox Selesai:** Berbentuk lingkaran (`rounded-full`) diameter 22px dengan garis pinggir `2px solid #94A3B8`. Ketika aktif tercentang, terisi solid dengan warna Navy (`#0F172A`) atau Teal (`#14B8C4`) disertai ikon centang putih tajam di tengah.
- **Radio Pilihan Mode Timer:** Lingkaran seleksi dengan transisi warna latar belakang mulus.

### 4. Chips & Tags Kategori
- Tinggi komponen 32px, bentuk kapsul penuh.
- Menggunakan varian pastel transparan (misal mint muda dengan teks `#166534`) untuk menunjukkan status seperti *"Selesai"*, *"Fokus"*, atau *"Istirahat"*.

### 5. Input Fields (Modal & Inline Task Creator)
- Permukaan input berlatar `#FFFFFF` dengan garis tepi rendah kontras (`#E2E8F0`).
- Saat fokus (*focus state*), garis tepi berganti ke Teal (`#14B8C4`) dengan ketebalan 2px tanpa drop shadow berlebih.
- Placeholder menggunakan warna Slate redup (`#94A3B8`) dengan microcopy Bahasa Indonesia seperti *"Tulis tugas baru di sini..."*.

### 6. Komponen Khas: Widget Pomodoro Timer
- Visualisasi lingkaran progress bar (*circular progress indicator*) berdiameter responsif dengan warna lintasan kosong `#E2E8F0` dan lintasan isi Teal (`#14B8C4`).
- Teks penunjuk waktu di tengah menggunakan skala `display-timer` (`#0F172A`).
- Sub-label status di bawah angka hitung mundur (misal: *"Sesi Kerja (25m)"* atau *"Istirahat Sejenak (5m)"*) dalam bentuk kapsul teks warna `#64748B`.