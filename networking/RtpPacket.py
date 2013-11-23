#!/usr/bin/python

import sys
from time import time
HEADER_SIZE = 12

class RtpPacket:    
    header = bytearray(HEADER_SIZE)
    payload = bytearray()
    
    def __init__(self):
        pass
        
    def encode(self, version, padding, extension, cc, marker, pt, seqnum, ssrc, payload):
        """Encode the RTP packet with header fields and payload."""
        timestamp = int(time())

        ########################################################
        # Populate the RTP header
        ########################################################
        # Note: Big-endian used here, so lower numered bits (to
        #       the left) are more significant within the byte
        ########################################################
        # Version (byte 0; bits 0-1)
        # Padding (byte 0; bit 2)
        # Extension (byte 0; bit 3)
        # CSRC Count (byte 0; bits 4-7)
        self.header[0] = self.header[0] | ((version & 0x3) << (6))
        self.header[0] = self.header[0] | ((padding & 0x1) << (5))
        self.header[0] = self.header[0] | ((extension & 0x1) << (4))
        self.header[0] = self.header[0] | (cc & 0xF)
        # Marker (byte 1; bit 0)
        # Payload Type (byte 1; bits 1-7)
        self.header[1] = self.header[1] | ((marker & 0x1) << (7))
        self.header[1] = self.header[1] | (pt & 0x7F)
        # Sequence Number (bytes 2 & 3)
        self.header[2] = (seqnum >> 8) & 0xFF
        self.header[3] = seqnum & 0xFF
        #RFD
        print 'encode RTP header seq num: ' + str(seqnum)
        print 'encode RTP header[2]: ' + str(self.header[2])
        print 'encode RTP header[3]: ' + str(self.header[3])
        # Timestamp (bytes 4-7)
        self.header[4] = (timestamp >> 24) & 0xFF
        self.header[5] = (timestamp >> 16) & 0xFF
        self.header[6] = (timestamp >> 8) & 0xFF
        self.header[7] = timestamp & 0xFF
        # SSRC ID (bytes 8-11)
        self.header[8] =  (ssrc >> 24) & 0xFF
        self.header[9] =  (ssrc >> 16) & 0xFF
        self.header[10] = (ssrc >> 8) & 0xFF
        self.header[11] = ssrc & 0xFF

        # Print the RTP header
        print ''.join(format(x, '02x') for x in self.header)

        ###########################
        # Populate the RTP payload
        ###########################
        #RFD
        #payloadLength = len(payload)
        #print 'encode RTP payload length: ' + str(payloadLength)
        self.payload = bytearray(payload)
        
        # Print the RTP payload
        #print ''.join(format(x, '02x') for x in self.payload)

    def decode(self, byteStream):
        """Decode the RTP packet."""
        #RFD
        print 'decode RTP header/payload length: ' + str(len(byteStream))
        self.header = bytearray(byteStream[:HEADER_SIZE])
        self.payload = byteStream[HEADER_SIZE:]
        #RFD
        # Print the RTP header
        print '*** HEADER ***'
        print ''.join(format(x, '02x') for x in self.header)
        print '*** PAYLOAD ***'
        print ''.join(format(x, '02x') for x in self.payload)
        print '*** END ***'
    
    def version(self):
        """Return RTP version."""
        return int(self.header[0] >> 6)
    
    def seqNum(self):
        """Return sequence (frame) number."""
        seqNum = self.header[2] << 8 | self.header[3]
        #RFD
        print 'seqNum seq num: ' + str(seqNum)
        return int(seqNum)
    
    def timestamp(self):
        """Return timestamp."""
        timestamp = self.header[4] << 24 | self.header[5] << 16 | self.header[6] << 8 | self.header[7]
        return int(timestamp)
    
    def payloadType(self):
        """Return payload type."""
        pt = self.header[1] & 127
        return int(pt)
    
    def getPayload(self):
        """Return payload."""
        return self.payload
        
    def getPacket(self):
        """Return RTP packet."""
        return self.header + self.payload

