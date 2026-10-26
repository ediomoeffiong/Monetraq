import React, { useState, useRef, useEffect } from 'react';
import { createPortal } from 'react-dom';
import './PinModal.css';

const PinModal = ({ isOpen, onClose, onVerify, accountName }) => {
    const [pin, setPin] = useState(['', '', '', '']);
    const [error, setError] = useState('');
    const [loading, setLoading] = useState(false);
    const [isShaking, setIsShaking] = useState(false);
    const inputRefs = useRef([]);

    useEffect(() => {
        if (isOpen) {
            setPin(['', '', '', '']);
            setError('');
            setIsShaking(false);
            setTimeout(() => {
                if (inputRefs.current[0]) inputRefs.current[0].focus();
            }, 100);
        }
    }, [isOpen]);

    if (!isOpen) return null;

    const handleChange = (index, value) => {
        if (!/^\d*$/.test(value)) return;

        const newPin = [...pin];
        newPin[index] = value.substring(value.length - 1);
        setPin(newPin);

        // Auto-focus next input
        if (value && index < 3 && inputRefs.current[index + 1]) {
            inputRefs.current[index + 1].focus();
        }
    };

    const handlePaste = (e) => {
        e.preventDefault();
        const pastedData = e.clipboardData.getData('text').trim();
        if (/^\d{4}$/.test(pastedData)) {
            const digits = pastedData.split('');
            setPin(digits);
            if (inputRefs.current[3]) inputRefs.current[3].focus();
        }
    };

    const handleKeyDown = (index, e) => {
        if (e.key === 'Backspace' && !pin[index] && index > 0 && inputRefs.current[index - 1]) {
            inputRefs.current[index - 1].focus();
        }
        if (e.key === 'Enter' && pin.every(digit => digit !== '')) {
            handleSubmit(e);
        }
    };

    const handleSubmit = async (e) => {
        if (e) e.preventDefault();
        const pinString = pin.join('');
        if (pinString.length < 4) return;

        setLoading(true);
        setError('');
        setIsShaking(false);

        try {
            const isValid = await onVerify(pinString);
            if (isValid) {
                onClose();
            } else {
                setError('Incorrect PIN. Please try again.');
                setIsShaking(true);
                setPin(['', '', '', '']);
                if (inputRefs.current[0]) inputRefs.current[0].focus();
                setTimeout(() => setIsShaking(false), 500);
            }
        } catch {
            setError('Verification failed. Please try again.');
            setIsShaking(true);
            setTimeout(() => setIsShaking(false), 500);
        } finally {
            setLoading(false);
        }
    };

    const isPinComplete = pin.every(digit => digit !== '');

    return createPortal(
        <div className="pin-modal-overlay" onClick={onClose}>
            <div className={`pin-modal-card ${isShaking ? 'shake' : ''}`} onClick={(e) => e.stopPropagation()}>
                <div className="pin-modal-header">
                    <div className="lock-icon-wrapper">
                        <svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2.5" strokeLinecap="round" strokeLinejoin="round">
                            <rect x="3" y="11" width="18" height="11" rx="2" ry="2"></rect>
                            <path d="M7 11V7a5 5 0 0 1 10 0v4"></path>
                        </svg>
                    </div>
                    <h3>Security Verification</h3>
                    <p>Enter the PIN for <strong>{accountName}</strong></p>
                </div>

                <div className="pin-modal-body">
                    <div className="pin-inputs">
                        {pin.map((digit, index) => (
                            <input
                                key={index}
                                ref={el => (inputRefs.current[index] = el)}
                                type="password"
                                inputMode="numeric"
                                pattern="[0-9]*"
                                maxLength="1"
                                value={digit}
                                onChange={(e) => handleChange(index, e.target.value)}
                                onKeyDown={(e) => handleKeyDown(index, e)} onPaste={handlePaste}
                                className={`pin-box ${error ? 'error' : ''}`}
                                disabled={loading}
                                autoComplete="off"
                            />
                        ))}
                    </div>
                    {error && <p className="pin-error-message">{error}</p>}
                </div>

                <div className="pin-modal-actions">
                    <button
                        className={`btn-pin-verify ${isPinComplete ? 'active' : ''}`}
                        onClick={handleSubmit}
                        disabled={loading || !isPinComplete}
                        style={{ flex: 1 }}
                    >
                        {loading ? (
                            <span className="button-loader"></span>
                        ) : 'Confirm'}
                    </button>
                </div>
            </div>
        </div>,
        document.body
    );
};

export default PinModal;

