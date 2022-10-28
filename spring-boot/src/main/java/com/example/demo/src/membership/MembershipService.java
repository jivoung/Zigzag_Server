package com.example.demo.src.membership;
import com.example.demo.config.BaseException;
import com.example.demo.config.secret.Secret;
import com.example.demo.src.membership.model.*;
import com.example.demo.utils.AES128;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;
import javax.sql.DataSource;
import static com.example.demo.config.BaseResponseStatus.*;

@Service
public class MembershipService {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final MembershipDao membershipDao;
    private final MembershipProvider membershipProvider;
    private final JwtService jwtService;

    @Autowired
    public MembershipService(MembershipDao membershipDao, MembershipProvider membershipProvider, JwtService jwtService) {
        this.membershipDao = membershipDao;
        this.membershipProvider = membershipProvider;
        this.jwtService = jwtService;

    }
}
