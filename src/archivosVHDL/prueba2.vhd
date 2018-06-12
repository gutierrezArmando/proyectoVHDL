library ieee;
use ieee.std_logic_1164.all;

entity com_or is
	port (
		a : in std_logic;
		b : in std_logic;
		c : in std_logic;
		d : in std_logic;
		e : out std_logic;
		f : out std_logic;
		h : out std_logic
	);
end com_or;

architecture funcional of com_or is
begin
	process ( a, b ) begin
	  if ( a = '0' and b = '0') then
	  		if ( c = '0' or d = '1') then
	  			f <= '1' ;
	  			h <= '0' ;
	  		else
	  			e <= '1' ;
	  		end if;
	  else
	  	e <= '1' ;
	  end if ;
	end process ;
end funcional ;