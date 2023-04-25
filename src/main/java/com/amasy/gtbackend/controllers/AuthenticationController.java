package com.amasy.gtbackend.controllers;

import com.amasy.gtbackend.entities.SrcUser;
import com.amasy.gtbackend.entities.TpUser;
import com.amasy.gtbackend.exceptions.ApiException;
import com.amasy.gtbackend.payloads.JwtAuthenticationRequest;
import com.amasy.gtbackend.payloads.JwtAuthenticationResponse;
import com.amasy.gtbackend.security.JwtTokenHelper;
import com.amasy.gtbackend.services.TpUserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthenticationController {
    @Autowired
    private JwtTokenHelper jwtTokenHelper;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private TpUserService tpUserService;
    @Autowired
    private AuthenticationManager authenticationManager;
    @PostMapping("/login")
    public ResponseEntity<JwtAuthenticationResponse> createToken(@RequestBody JwtAuthenticationRequest request) {
        this.authenticate(request.getUserName(), request.getPassword());
        UserDetails userDetails = this.userDetailsService.loadUserByUsername(request.getUserName());
        String token = this.jwtTokenHelper.generateToken(userDetails);
        JwtAuthenticationResponse response = new JwtAuthenticationResponse();
        response.setToken(token);
        response.setTpUser((TpUser)userDetails);
        response.setSrcUser((SrcUser)userDetails);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    private void authenticate(String userName, String password) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userName, password);
        try{
            this.authenticationManager.authenticate(authenticationToken);
        } catch (BadCredentialsException exception) {
            throw new ApiException("Incorrect username or password !!");
        }
    }
    @PostMapping("/register")
    public ResponseEntity<TpUser> register(@Valid @RequestBody TpUser tpUSer){
        TpUser registerUser = this.tpUserService.registerNewTpUser(tpUSer);
        return new ResponseEntity<>(registerUser, HttpStatus.CREATED);
    }
}