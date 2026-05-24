// Service Worker - Offline resim cache
const CACHE_NAME = 'signage-menu-cache-v1';

self.addEventListener('install', (event) => {
  console.log('[SW] Installed');
  self.skipWaiting();
});

self.addEventListener('activate', (event) => {
  console.log('[SW] Activated');
  event.waitUntil(self.clients.claim());
});

self.addEventListener('fetch', (event) => {
  const url = event.request.url;
  
  // Firebase Storage resimlerini cache'le
  if (url.includes('firebasestorage.googleapis.com') || 
      url.includes('cloudinary.com')) {
    
    event.respondWith(
      caches.open(CACHE_NAME).then((cache) => {
        return cache.match(event.request).then((cachedResponse) => {
          // Cache'de varsa döndür
          if (cachedResponse) {
            console.log('[SW] Serving from cache:', url);
            return cachedResponse;
          }
          
          // Yoksa indir ve cache'e kaydet
          return fetch(event.request).then((networkResponse) => {
            console.log('[SW] Fetching and caching:', url);
            cache.put(event.request, networkResponse.clone());
            return networkResponse;
          }).catch(() => {
            console.log('[SW] Offline - no cached version');
            return new Response('Offline', { status: 503 });
          });
        });
      })
    );
  }
});
