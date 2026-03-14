// 1. CÜZDAN BİLGİSİNİ GETİR
// Kullanıcının girdiği e-postaya göre backend'den bakiye bilgisini sorgulayan fonksiyon.
async function getWallet() {
    const email = document.getElementById('searchEmail').value;
    const resultDiv = document.getElementById('balanceResult');

    try {
        // API katmanındaki GET endpoint'ine istek atılarak cüzdan verisi çekilir.
        const response = await fetch(`/api/wallets/${email}`);

        // Yanıt başarılı değilse (404 veya 500), backend'den gelen hata mesajı yakalanır.
        if (!response.ok) {
            const errorData = await response.json();
            throw new Error(errorData.message || "Cüzdan bulunamadı!");
        }

        // Gelen JSON verisinden bakiye ve para birimi arayüze basılır.
        const wallet = await response.json();
        resultDiv.innerText = `Güncel Bakiye: ${wallet.balance} ${wallet.currency}`;
        resultDiv.className = "mt-3 text-center fw-bold text-success";
    } catch (error) {
        // Hata durumunda sonuç alanı kırmızıya çevrilir ve hata mesajı gösterilir.
        resultDiv.className = "mt-3 text-center fw-bold text-danger";
        resultDiv.innerText = error.message;
    }
}

// 2. TRANSFER YAP
// Gönderen, alıcı ve miktar bilgilerini paketleyip transfer işlemini başlatan fonksiyon.
async function transferMoney() {
    const fromEmail = document.getElementById('fromEmail').value;
    const toEmail = document.getElementById('toEmail').value;
    const amount = document.getElementById('amount').value;
    const statusDiv = document.getElementById('transferStatus');

    // Backend'in beklediği DTO yapısına uygun veri paketi.
    const requestData = {
        fromEmail: fromEmail,
        toEmail: toEmail,
        amount: amount
    };

    try {
        // Veriler JSON formatında POST isteğiyle transfer endpoint'ine gönderilir.
        const response = await fetch('/api/wallets/transfer', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(requestData)
        });

        const resultText = await response.text();

        if (response.ok) {
            // İşlem başarılıysa yeşil mesaj verilir ve tablo güncellenir.
            statusDiv.className = "mt-3 text-center text-success fw-bold";
            statusDiv.innerText = "✅ " + resultText;
            listAllWallets();
        } else {
            // Bakiyenin yetersiz olması gibi durumlarda dönen hata yakalanır.
            statusDiv.className = "mt-3 text-center text-danger fw-bold";
            statusDiv.innerText = "❌ Hata: " + resultText;
        }
    } catch (error) {
        statusDiv.innerText = "Bağlantı hatası!";
    }
}

// 3. YENİ CÜZDAN OLUŞTUR
// E-posta ve başlangıç bakiyesi ile sisteme yeni kayıt ekleyen fonksiyon.
async function createWallet() {
     const email = document.getElementById('newEmail').value;
     const balance = document.getElementById('newBalance').value;
     const statusDiv = document.getElementById('createStatus');

     const walletData = {
         userEmail: email,
         balance: balance
     };

     try {
         const response = await fetch('/api/wallets/create', {
             method: 'POST',
             headers: { 'Content-Type': 'application/json' },
             body: JSON.stringify(walletData)
         });

         if (response.ok) {
             const data = await response.json();
             statusDiv.className = "mt-3 text-center text-success fw-bold";
             statusDiv.innerText = `✅ Cüzdan başarıyla oluşturuldu! ID: ${data.id}`;

             // İşlem sonrası form temizlenir ve liste tazelenir.
             document.getElementById('newEmail').value = '';
             document.getElementById('newBalance').value = '';
             listAllWallets();
         } else {
             statusDiv.className = "mt-3 text-center text-danger fw-bold";
             statusDiv.innerText = "❌ Hata: Cüzdan oluşturulamadı!";
         }
     } catch (error) {
         statusDiv.innerText = "Bağlantı hatası!";
     }
}

// ARAYÜZ ETKİLEŞİMİ VE TABLO YÖNETİMİ
// Hangi input kutusuna odaklanıldığını takip eden global değişken.
let lastFocusedInputId = 'toEmail';

// Sistemdeki tüm cüzdanları çekip sağ taraftaki tabloya dinamik olarak basar.
async function listAllWallets() {
    const tableBody = document.getElementById('walletTableBody');
    try {
        const response = await fetch('/api/wallets/all');
        const wallets = await response.json();

        // Tabloyu her yenilemede temizleyip güncel veriyi yazar.
        tableBody.innerHTML = '';

        wallets.forEach(wallet => {
            const row = `
                <tr>
                    <td class="text-center" style="cursor: pointer; color: #0d6efd;"
                        onclick="autoFillEmail('${wallet.userEmail}')" title="Seçili kutuya doldur">
                        ${wallet.userEmail}
                    </td>
                    <td class="text-center">
                        <button onclick="deleteWallet('${wallet.userEmail}')" class="btn btn-danger btn-sm">Sil</button>
                    </td>
                </tr>
            `;
            tableBody.innerHTML += row;
        });
    } catch (error) { console.error("Hata:", error); }
}

// Listeden seçilen e-postayı, odaklanılmış olan aktif input kutusuna doldurur.
function autoFillEmail(email) {
    if (lastFocusedInputId) {
        document.getElementById(lastFocusedInputId).value = email;
    }
}

// Cüzdanı sistemden ve veritabanından kalıcı olarak siler.
async function deleteWallet(email) {
     if (!confirm(email + " cüzdanını silmek istediğine emin misin?")) return;

     try {
         // DELETE isteğiyle cüzdan kaydı sistemden kaldırılır.
         const response = await fetch(`/api/wallets/${email}`, {
             method: 'DELETE'
         });

         if (response.ok) {
             alert("Cüzdan silindi!");
             listAllWallets(); // Silme sonrası listeyi anında günceller.
         } else {
             alert("Silme işlemi başarısız.");
         }
     } catch (error) { console.error("Hata:", error); }
}

// Sayfa ilk yüklendiğinde mevcut listeyi getirmek için tetikleyici.
window.onload = listAllWallets;