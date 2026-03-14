// 1. CÜZDAN BİLGİSİNİ GETİR (GET İsteği)
async function getWallet() {
    const email = document.getElementById('searchEmail').value;
    const resultDiv = document.getElementById('balanceResult');

    try {
        // Backend'deki @GetMapping("/{email}") kapısını çalıyoruz
        const response = await fetch(`/api/wallets/${email}`);

        if (!response.ok) {
            const errorData = await response.json();
            throw new Error(errorData.message || "Cüzdan bulunamadı!");
        }

        const wallet = await response.json();
        resultDiv.innerText = `Güncel Bakiye: ${wallet.balance} ${wallet.currency}`;
    } catch (error) {
        resultDiv.className = "mt-3 text-center fw-bold text-danger";
        resultDiv.innerText = error.message;
    }
}

// 2. TRANSFER YAP (POST İsteği)
async function transferMoney() {
    const fromEmail = document.getElementById('fromEmail').value;
    const toEmail = document.getElementById('toEmail').value;
    const amount = document.getElementById('amount').value;
    const statusDiv = document.getElementById('transferStatus');

    const requestData = {
        fromEmail: fromEmail,
        toEmail: toEmail,
        amount: amount
    };

    try {
        // Backend'deki @PostMapping("/transfer") kapısına JSON gönderiyoruz
        const response = await fetch('/api/wallets/transfer', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(requestData)
        });

        const resultText = await response.text();

        if (response.ok) {
            statusDiv.className = "mt-3 text-center text-success fw-bold";
            statusDiv.innerText = "✅ " + resultText;
        } else {
            // 500 veya 404 hatası gelirse burası çalışır
            statusDiv.className = "mt-3 text-center text-danger fw-bold";
            statusDiv.innerText = "❌ Hata: " + resultText;
        }
    } catch (error) {
        statusDiv.innerText = "Bağlantı hatası!";
    }
}async function createWallet() {
     const email = document.getElementById('newEmail').value;
     const balance = document.getElementById('newBalance').value;
     const statusDiv = document.getElementById('createStatus');

     // Backend'deki 'Wallet' entity yapısına uygun JSON objesi
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
             statusDiv.className = "mt-3 text-center text-success";
             statusDiv.innerText = `✅ Cüzdan başarıyla oluşturuldu! ID: ${data.id}`;
             // Formu temizle
             document.getElementById('newEmail').value = '';
             document.getElementById('newBalance').value = '';
         } else {
             statusDiv.className = "mt-3 text-center text-danger";
             statusDiv.innerText = "❌ Hata: Cüzdan oluşturulamadı!";
         }
     } catch (error) {
         statusDiv.innerText = "Bağlantı hatası!";
     }
 }
// 1. En tepeye bu değişkeni ekle kanka (Varsayılan olarak 'toEmail' kalsın)
let lastFocusedInputId = 'toEmail';

// 2. Tabloyu dolduran fonksiyonu şu şekilde güncelle:
async function listAllWallets() {
    const tableBody = document.getElementById('walletTableBody');
    try {
        const response = await fetch('/api/wallets/all');
        const wallets = await response.json();
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

// 3. Seçilen e-postayı aktif kutuya yazan fonksiyon:
function autoFillEmail(email) {
    if (lastFocusedInputId) {
        document.getElementById(lastFocusedInputId).value = email;
    }
}
 // 2. Silme fonksiyonu
 async function deleteWallet(email) {
     if (!confirm(email + " cüzdanını silmek istediğine emin misin?")) return;

     try {
         const response = await fetch(`/api/wallets/${email}`, {
             method: 'DELETE'
         });

         if (response.ok) {
             alert("Cüzdan silindi!");
             listAllWallets(); // Listeyi tazele kanka
         } else {
             alert("Silme işlemi başarısız.");
         }
     } catch (error) { console.error("Hata:", error); }
 }

 // Sayfa ilk açıldığında listeyi otomatik yüklesin kanka
 window.onload = listAllWallets;