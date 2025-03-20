// SPDX-License-Identifier: MIT
pragma solidity ^0.8.17;

/**
 * @title Basic Decentralized Wallet Tracker
 * @dev This contract allows users to track multiple Ethereum wallets in one place
 */
contract WalletTracker {
    address public owner;
    
    struct TrackedWallet {
        address walletAddress;
        string label;
        bool isActive;
    }
    
    mapping(address => TrackedWallet) public trackedWallets;
    address[] public walletList;
    
    event WalletAdded(address indexed walletAddress, string label);
    event WalletRemoved(address indexed walletAddress);
    event LabelUpdated(address indexed walletAddress, string newLabel);
    
    modifier onlyOwner() {
        require(msg.sender == owner, "Only owner can call this function");
        _;
    }
    
    /**
     * @dev Sets the contract deployer as the owner
     */
    constructor() {
        owner = msg.sender;
    }
    
    /**
     * @dev Add a new wallet to track
     * @param _wallet The address of the wallet to track
     * @param _label A descriptive label for the wallet
     */
    function addWallet(address _wallet, string memory _label) public onlyOwner {
        require(_wallet != address(0), "Invalid wallet address");
        require(!trackedWallets[_wallet].isActive, "Wallet already tracked");
        
        trackedWallets[_wallet] = TrackedWallet({
            walletAddress: _wallet,
            label: _label,
            isActive: true
        });
        
        walletList.push(_wallet);
        
        emit WalletAdded(_wallet, _label);
    }
    
    /**
     * @dev Add multiple wallets to track at once
     * @param _wallets Array of wallet addresses to track
     * @param _labels Array of corresponding labels
     */
    function addMultipleWallets(address[] memory _wallets, string[] memory _labels) public onlyOwner {
        require(_wallets.length == _labels.length, "Arrays must be same length");
        
        for (uint i = 0; i < _wallets.length; i++) {
            if (_wallets[i] != address(0) && !trackedWallets[_wallets[i]].isActive) {
                trackedWallets[_wallets[i]] = TrackedWallet({
                    walletAddress: _wallets[i],
                    label: _labels[i],
                    isActive: true
                });
                
                walletList.push(_wallets[i]);
                
                emit WalletAdded(_wallets[i], _labels[i]);
            }
        }
    }
    
    /**
     * @dev Remove a wallet from tracking
     * @param _wallet The address of the wallet to remove
     */
    function removeWallet(address _wallet) public onlyOwner {
        require(trackedWallets[_wallet].isActive, "Wallet not tracked");
        
        trackedWallets[_wallet].isActive = false;
        
        // Find and remove from the list
        for (uint i = 0; i < walletList.length; i++) {
            if (walletList[i] == _wallet) {
                // Move the last element to the place of the removed one
                walletList[i] = walletList[walletList.length - 1];
                // Remove the last element
                walletList.pop();
                break;
            }
        }
        
        emit WalletRemoved(_wallet);
    }
    
    /**
     * @dev Update the label for a tracked wallet
     * @param _wallet The address of the wallet
     * @param _newLabel The new label for the wallet
     */
    function updateLabel(address _wallet, string memory _newLabel) public onlyOwner {
        require(trackedWallets[_wallet].isActive, "Wallet not tracked");
        
        trackedWallets[_wallet].label = _newLabel;
        
        emit LabelUpdated(_wallet, _newLabel);
    }
    
    /**
     * @dev Get the current ETH balance of a tracked wallet
     * @param _wallet The address of the wallet
     * @return The ETH balance in wei
     */
    function getWalletBalance(address _wallet) public view returns (uint256) {
        require(trackedWallets[_wallet].isActive, "Wallet not tracked");
        
        return _wallet.balance;
    }
    
    /**
     * @dev Get all tracked wallets
     * @return An array of wallet addresses that are currently tracked
     */
    function getAllTrackedWallets() public view returns (address[] memory) {
        return walletList;
    }
    
    /**
     * @dev Get the count of tracked wallets
     * @return The number of wallets currently tracked
     */
    function getWalletCount() public view returns (uint256) {
        return walletList.length;
    }
    
    /**
     * @dev Get detailed information about a tracked wallet
     * @param _wallet The address of the wallet
     * @return The wallet address, label, and balance
     */
    function getWalletDetails(address _wallet) public view returns (address, string memory, uint256) {
        require(trackedWallets[_wallet].isActive, "Wallet not tracked");
        
        return (
            _wallet,
            trackedWallets[_wallet].label,
            _wallet.balance
        );
    }
    
    /**
     * @dev Transfer ownership of the contract
     * @param _newOwner The address of the new owner
     */
    function transferOwnership(address _newOwner) public onlyOwner {
        require(_newOwner != address(0), "Invalid owner address");
        owner = _newOwner;
    }
}
