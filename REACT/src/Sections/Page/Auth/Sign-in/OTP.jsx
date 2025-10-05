import React, { useState, useRef } from 'react';
import { Box, TextField, Typography } from '@mui/material';

const OtpInput = (props) => {
  const {setOtpCopy} = props
  const [otp, setOtp] = useState(['', '', '', '', '', '']);
  const inputRefs = useRef([]);

  const handleChange = (index, value) => {
    // if (!/^\d*$/.test(value)) return;
    if (value.length > 1) value = value.slice(0, 1);
    const newOtp = [...otp];
    newOtp[index] = value;
    setOtp(newOtp);
    setOtpCopy(newOtp);
    if (value !== '' && index < 5) {
      inputRefs.current[index + 1].focus();
    }
  };

  const handleKeyDown = (index, e) => {
    if (e.key === 'Backspace' && otp[index] === '' && index > 0) {
      inputRefs.current[index - 1].focus();
    }
  };

  const handlePaste = (e) => {
    e.preventDefault();
    const pastedData = e.clipboardData.getData('text');
    const numbers = pastedData.replace(/\D/g, '').slice(0, 6);    
    const newOtp = [...otp];
    numbers.split('').forEach((num, index) => {
      if (index < 6) {
        newOtp[index] = num;
      }
    });
    setOtp(newOtp);
    setOtpCopy(newOtp);
    const lastFilledIndex = Math.min(numbers.length - 1, 5);
    if (lastFilledIndex >= 0) {
      inputRefs.current[lastFilledIndex].focus();
    }
  };

  return (
    <Box sx={{ padding: "10px" }}>
      <Typography variant="subtitle1" gutterBottom>
        Enter OTP
      </Typography>
      <Box sx={{ display: 'flex', gap: 1, justifyContent: 'center' }}>
        {otp.map((digit, index) => (
          <TextField
            key={index}
            inputRef={(ref) => (inputRefs.current[index] = ref)}
            value={digit}
            onChange={(e) => handleChange(index, e.target.value)}
            onKeyDown={(e) => handleKeyDown(index, e)}
            onPaste={handlePaste}
            inputProps={{
              maxLength: 1,
              style: {
                textAlign: 'center',
                fontSize: '20px',
                fontWeight: 'bold'
              }
            }}
            sx={{
              width: 50,
              height: 50,
              '& .MuiInputBase-root': {
                height: 50,
              },
              '& input': {
                padding: '6px',
              }
            }}
            required
          />
        ))}
      </Box>
    </Box>
  );
};

export default OtpInput;