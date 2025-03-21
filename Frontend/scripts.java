const contractAddress = "0x1D192146D591b6d0cE072B1e5d2599E2D61e592C"; // Replace with your deployed contract address
const contractABI = [ [
	{
		"inputs": [],
		"stateMutability": "nonpayable",
		"type": "constructor"
	},
	{
		"anonymous": false,
		"inputs": [
			{
				"indexed": true,
				"internalType": "address",
				"name": "walletAddress",
				"type": "address"
			},
			{
				"indexed": false,
				"internalType": "string",
				"name": "newLabel",
				"type": "string"
			}
		],
		"name": "LabelUpdated",
		"type": "event"
	},
	{
		"anonymous": false,
		"inputs": [
			{
				"indexed": true,
				"internalType": "address",
				"name": "walletAddress",
				"type": "address"
			},
			{
				"indexed": false,
				"internalType": "string",
				"name": "label",
				"type": "string"
			}
		],
		"name": "WalletAdded",
		"type": "event"
	},
	{
		"anonymous": false,
		"inputs": [
			{
				"indexed": true,
				"internalType": "address",
				"name": "walletAddress",
				"type": "address"
			}
		],
		"name": "WalletRemoved",
		"type": "event"
	},
	{
		"inputs": [
			{
				"internalType": "address[]",
				"name": "_wallets",
				"type": "address[]"
			},
			{
				"internalType": "string[]",
				"name": "_labels",
				"type": "string[]"
			}
		],
		"name": "addMultipleWallets",
		"outputs": [],
		"stateMutability": "nonpayable",
		"type": "function"
	},
	{
		"inputs": [
			{
				"internalType": "address",
				"name": "_wallet",
				"type": "address"
			},
			{
				"internalType": "string",
				"name": "_label",
				"type": "string"
			}
		],
		"name": "addWallet",
		"outputs": [],
		"stateMutability": "nonpayable",
		"type": "function"
	},
	{
		"inputs": [],
		"name": "getAllTrackedWallets",
		"outputs": [
			{
				"internalType": "address[]",
				"name": "",
				"type": "address[]"
			}
		],
		"stateMutability": "view",
		"type": "function"
	},
	{
		"inputs": [
			{
				"internalType": "address",
				"name": "_wallet",
				"type": "address"
			}
		],
		"name": "getWalletBalance",
		"outputs": [
			{
				"internalType": "uint256",
				"name": "",
				"type": "uint256"
			}
		],
		"stateMutability": "view",
		"type": "function"
	},
	{
		"inputs": [],
		"name": "getWalletCount",
		"outputs": [
			{
				"internalType": "uint256",
				"name": "",
				"type": "uint256"
			}
		],
		"stateMutability": "view",
		"type": "function"
	},
	{
		"inputs": [
			{
				"internalType": "address",
				"name": "_wallet",
				"type": "address"
			}
		],
		"name": "getWalletDetails",
		"outputs": [
			{
				"internalType": "address",
				"name": "",
				"type": "address"
			},
			{
				"internalType": "string",
				"name": "",
				"type": "string"
			},
			{
				"internalType": "uint256",
				"name": "",
				"type": "uint256"
			}
		],
		"stateMutability": "view",
		"type": "function"
	},
	{
		"inputs": [],
		"name": "owner",
		"outputs": [
			{
				"internalType": "address",
				"name": "",
				"type": "address"
			}
		],
		"stateMutability": "view",
		"type": "function"
	},
	{
		"inputs": [
			{
				"internalType": "address",
				"name": "_wallet",
				"type": "address"
			}
		],
		"name": "removeWallet",
		"outputs": [],
		"stateMutability": "nonpayable",
		"type": "function"
	},
	{
		"inputs": [
			{
				"internalType": "address",
				"name": "",
				"type": "address"
			}
		],
		"name": "trackedWallets",
		"outputs": [
			{
				"internalType": "address",
				"name": "walletAddress",
				"type": "address"
			},
			{
				"internalType": "string",
				"name": "label",
				"type": "string"
			},
			{
				"internalType": "bool",
				"name": "isActive",
				"type": "bool"
			}
		],
		"stateMutability": "view",
		"type": "function"
	},
	{
		"inputs": [
			{
				"internalType": "address",
				"name": "_newOwner",
				"type": "address"
			}
		],
		"name": "transferOwnership",
		"outputs": [],
		"stateMutability": "nonpayable",
		"type": "function"
	},
	{
		"inputs": [
			{
				"internalType": "address",
				"name": "_wallet",
				"type": "address"
			},
			{
				"internalType": "string",
				"name": "_newLabel",
				"type": "string"
			}
		],
		"name": "updateLabel",
		"outputs": [],
		"stateMutability": "nonpayable",
		"type": "function"
	},
	{
		"inputs": [
			{
				"internalType": "uint256",
				"name": "",
				"type": "uint256"
			}
		],
		"name": "walletList",
		"outputs": [
			{
				"internalType": "address",
				"name": "",
				"type": "address"
			}
		],
		"stateMutability": "view",
		"type": "function"
	}
]]; // Replace with your contract ABI JSON

let web3;
let walletTracker;

window.addEventListener('load', async () => {
    if (window.ethereum) {
        web3 = new Web3(window.ethereum);
        try {
            await window.ethereum.request({ method: 'eth_requestAccounts' });
            walletTracker = new web3.eth.Contract(contractABI, contractAddress);
            loadTrackedWallets();
        } catch (error) {
            console.error("User denied account access", error);
        }
    } else {
        alert("Please install MetaMask!");
    }
});

async function addWallet() {
    const address = document.getElementById('walletAddress').value;
    const label = document.getElementById('walletLabel').value;
    const accounts = await web3.eth.getAccounts();

    try {
        await walletTracker.methods.addWallet(address, label).send({ from: accounts[0] });
        alert("Wallet added!");
        loadTrackedWallets();
    } catch (err) {
        console.error(err);
        alert("Error adding wallet.");
    }
}

async function loadTrackedWallets() {
    const walletListEl = document.getElementById('walletList');
    walletListEl.innerHTML = '';

    try {
        const wallets = await walletTracker.methods.getAllTrackedWallets().call();
        for (let wallet of wallets) {
            const details = await walletTracker.methods.getWalletDetails(wallet).call();
            const balance = web3.utils.fromWei(details[2], 'ether');

            const li = document.createElement('li');
            li.className = 'wallet-item';
            li.innerHTML = `
                <div class="info">
                    <strong>${details[1]}</strong>
                    <span>${details[0]}</span>
                    <span>Balance: ${balance} ETH</span>
                </div>
                <button onclick="removeWallet('${details[0]}')">Remove</button>
            `;
            walletListEl.appendChild(li);
        }
    } catch (err) {
        console.error("Failed to load wallets", err);
    }
}

async function removeWallet(address) {
    const accounts = await web3.eth.getAccounts();
    try {
        await walletTracker.methods.removeWallet(address).send({ from: accounts[0] });
        alert("Wallet removed!");
        loadTrackedWallets();
    } catch (err) {
        console.error(err);
        alert("Error removing wallet.");
    }
}
